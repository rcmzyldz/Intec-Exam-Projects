package be.intec.utils;

import be.intec.models.Message;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

public class MessageUtils {

    private MessageUtils() {

    }

    public static Message saveMessageToDatabase(Message student) {

        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        student.setId(null);
        manager.getTransaction().begin();

        manager.persist(student);
        manager.getTransaction().commit();
        manager.close();

        return student;
    }

    public static Message findMessageById(Long id) {

        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        Message foundMessage = manager.find(Message.class, id);

        manager.close();

        return foundMessage;
    }

    public static Optional<Long> getLastMessageId() {

        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        // READ ALL STUDENT FIRST

        Long lastMessageId = null;
        Message studentWithLastId = null;
        try {
            studentWithLastId = (Message) manager
                    .createNativeQuery("SELECT * from message ORDER BY message_id DESC LIMIT 1", Message.class)
                    .getSingleResult();

        } catch(NoResultException noResultException) {

        }


        if(studentWithLastId != null){
            lastMessageId = studentWithLastId.getId();
        }

        manager.close();


        // GET LAST ELEMENT FROM THE LIST


        // RETURN ID FROM THE LAST ELEMENT

        return Optional.ofNullable(lastMessageId);
    }

}
