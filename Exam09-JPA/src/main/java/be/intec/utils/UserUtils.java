package be.intec.utils;

import be.intec.models.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

public class UserUtils {

    private UserUtils() {

    }

    public static User saveUserToDatabase(User user) {

        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        user.setId(null);
        manager.getTransaction().begin();

        manager.persist(user);
        manager.getTransaction().commit();
        manager.close();

        return user;
    }

    public static User findUserById(Long id) {

        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        User foundUser = manager.find(User.class, id);

        manager.close();

        return foundUser;
    }

    public static Optional<Long> getLastUserId() {

        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        // READ ALL STUDENT FIRST

        Long lastUserId = null;
        User studentWithLastId = null;
        try {
            studentWithLastId = (User) manager
                    .createNativeQuery("SELECT * from student ORDER BY student_id DESC LIMIT 1", User.class)
                    .getSingleResult();

        } catch(NoResultException noResultException) {

        }


        if(studentWithLastId != null){
            lastUserId = studentWithLastId.getId();
        }

        manager.close();


        // GET LAST ELEMENT FROM THE LIST


        // RETURN ID FROM THE LAST ELEMENT

        return Optional.ofNullable(lastUserId);
    }

}
