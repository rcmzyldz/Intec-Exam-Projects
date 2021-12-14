package be.intec.repository;

import be.intec.exceptions.ExceptionMessagesForMessages;
import be.intec.exceptions.QueryException;
import be.intec.models.Message;
import be.intec.models.User;
import be.intec.utils.JPAFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static be.intec.exceptions.ExceptionMessages.*;
import static be.intec.exceptions.ExceptionMessagesForMessages.*;
import static be.intec.exceptions.ExceptionMessagesForMessages.UNDEFINED_EXCEPTION;

public class MessageRepository implements IMessageRepository<Message, Long> {

    @Override
    public Long save(Message message) throws QueryException{

        if ( message == null ){
            throw new QueryException(MESSAGE_INFO_REQUIRED.getBody());
        }

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();


        // CREATE
        manager.persist(message);

        manager.getTransaction().commit();

        Long lastSavedId = message.getId();

        manager.close();

        return lastSavedId;

    }

    @Override
    public Long updateSubject(Long id, String content) throws QueryException {



        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();


        Message foundMessage = manager.find(Message.class, id);


        foundMessage.setSubject(content);

        // UPDATE ENTITY
        manager.merge(foundMessage);

        manager.getTransaction().commit();

        Long lastUpdatedId = foundMessage.getId();

        manager.close();

        return lastUpdatedId;

    }

    @Override
    public Long updateContent(Long id, String content) {




        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Message foundMessage = manager.find(Message.class, id);


        Message foundMessageByContent = null;
        try {
            foundMessageByContent = (Message) manager
                    .createNativeQuery("SELECT * FROM student WHERE email = :qpEmail", Message.class)
                    .setParameter("qpContent", content)
                    .getSingleResult();
        } catch (NoResultException ignored) {

        }

        if (foundMessageByContent != null) {
            throw new QueryException(MESSAGE_ALREADY_EXISTS.getBody());
        }

        foundMessage.setContent(content);

        // UPDATE ENTITY
        manager.merge(foundMessage);

        manager.getTransaction().commit();

        if (foundMessage.getId() == null) {
            throw new QueryException(UNDEFINED_EXCEPTION.getBody());
        }

        Long lastUpdatedId = foundMessage.getId();

        manager.close();

        return lastUpdatedId;

    }

    @Override
    public Long deleteById(Long id) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Message foundMessage = manager.find(Message.class, id);


        Long lastDeletedId = foundMessage.getId();

        // REMOVE THE ENTITY
        manager.remove(foundMessage);

        manager.getTransaction().commit();

        if (foundMessage.getId() != null) {
            throw new QueryException(ExceptionMessagesForMessages.UNDEFINED_EXCEPTION.getBody());
        }

        manager.close();

        return lastDeletedId;

    }

    @Override
    public Optional<Message> findById(Long id) throws QueryException{
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        // SEARCH ENTITY
        Message foundMessage = manager.find(Message.class, id);


        manager.close();

        return Optional.ofNullable(foundMessage);
    }

    @Override
    public List<Message> findAll(Long limit, Long offset) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        List<Message> messageList = Collections.unmodifiableList(
                manager.createNativeQuery("SELECT * FROM message LIMIT :qpLimit OFFSET :qpOffset", Message.class)
                        .setParameter("qpLimit", limit)
                        .setParameter("qpOffset", offset)
                        .getResultList());

        manager.close();

        return messageList;
    }

    @Override
    public List<Message> findAllBySender(Long fromUserId) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        List<Message> messageList = Collections.unmodifiableList(
                manager.createNativeQuery("SELECT * FROM message WHERE from_user_id = :qpFromUserId ", Message.class)
                        .setParameter("qpFromUserId", fromUserId)
                        .getResultList());

        manager.close();

        return messageList;
    }

    @Override
    public List<Message> findAllFromReceiver(Long toUserId) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        List<Message> messageList = Collections.unmodifiableList(
                manager.createNativeQuery("SELECT * FROM message WHERE message.to_user_id = :qpToUserId ", Message.class)
                        .setParameter("qpToUserId", toUserId)
                        .getResultList());

        manager.close();

        return messageList;
    }
}
