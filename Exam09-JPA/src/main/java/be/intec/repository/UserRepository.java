package be.intec.repository;

import be.intec.exceptions.QueryException;
import be.intec.models.User;
import be.intec.utils.JPAFactory;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static be.intec.exceptions.ExceptionMessages.*;

public class UserRepository implements IUserRepository<User, Long> {


    @Override
    public Long save(User user) throws QueryException {


        if ( user == null ){
            throw new QueryException(USER_INFO_REQUIRED.getBody());
        }
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();


        // CREATE
        manager.persist(user);

        manager.getTransaction().commit();

        Long lastSavedId = user.getId();

        manager.close();

        return lastSavedId;

    }

    @Override
    public Long updateEmail(Long id, String email) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        User foundUser = manager.find(User.class, id);


        foundUser.setEmail("joe@doe.be");

        // UPDATE ENTITY
        manager.merge(foundUser);

        manager.getTransaction().commit();

        Long lastUpdatedId = foundUser.getId();

        manager.close();

        return lastUpdatedId;


    }

    @Override
    public Long deleteById(Long id) throws QueryException  {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();


        User foundUser = manager.find(User.class, id);

      /*  if (foundUser == null) {
            throw new QueryException(USER_NOT_FOUND.getBody());
        }*/

        Long lastDeletedId = foundUser.getId();

        // REMOVE THE ENTITY
        manager.remove(foundUser);

        manager.getTransaction().commit();


        manager.close();

        return lastDeletedId;

    }

    @Override
        public Optional<User> findById (Long id) throws QueryException {

            if (id == null) {
                throw new QueryException(USER_ID_IS_REQUIRED.getBody());
            }

            if (id <= 0L) {
                throw new QueryException(USER_ID_IS_NOT_VALID.getBody());
            }

            EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

            // SEARCH ENTITY
            User foundUser = manager.find(User.class, id);

            manager.close();

            return Optional.ofNullable(foundUser);
        }



    @Override
    public List<User> findAll() throws QueryException {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        List<User> userList = Collections.unmodifiableList(
                manager.createNativeQuery("SELECT * FROM user", User.class)
                        .getResultList());

        manager.close();

        return userList;

    }

    @Override
    public List<User> search(String firstName, String lastName, String email) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        List<User> userList = Collections.unmodifiableList(manager
                .createNativeQuery("SELECT * FROM user WHERE firstName > :qpFirstName AND lastName > :qpLastName AND email > :qpEmail")
                .setParameter("qpFirstName", firstName)
                .setParameter("qpLastName", lastName)
                .setParameter("qpEmail", email)
                .getResultList()
        );

        manager.close();

        return userList;
    }

    @Override
    public User findByEmail(String email) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        User foundUser =null;
        try {
            foundUser = (User) manager
                    .createNativeQuery("SELECT * FROM user WHERE email = :qpEmail", User.class)
                    .setParameter("qpEmail", email)
                    .getSingleResult();
        } catch (NoResultException ignored) {

        }

        manager.close();

        manager.close();

        return foundUser;
    }

    @Override
    public User findByPhone(String phone) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        User foundUser =null;
        try {
            foundUser = (User) manager
                    .createNativeQuery("SELECT * FROM user WHERE phone = :qpPhone", User.class)
                    .setParameter("qpPhone", phone)
                    .getSingleResult();
        } catch (NoResultException ignored) {

        }

        manager.close();

        manager.close();

        return foundUser;
    }
}