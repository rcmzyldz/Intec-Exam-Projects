package be.intec.repository;

import be.intec.exceptions.ExceptionMessages;
import be.intec.exceptions.QueryException;
import be.intec.models.User;
import be.intec.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static be.intec.exceptions.ExceptionMessages.*;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private static int emailIndex = 0;

    private String nextUniqueEmail() {
        return ("user" + (++emailIndex) + "@mail.be");
    }

    private static int phoneIndex = 0;

    private String nextUniquePhoneNumber() {
        return ("04567" + (++phoneIndex));
    }

    private final UserRepository repository = new UserRepository();

    @Test
    void should_save_succeed() {

        User user = new User();
        user.setFirstName("Nikola");
        user.setLastName("Tesla");
        user.setEmail(nextUniqueEmail());
        user.setPasscode("1234");
        user.setPhone(nextUniquePhoneNumber());

        Long savedUserId = repository.save(user);

        assertNotNull(savedUserId);
        assertNotEquals(0L, savedUserId);

    }

    @Test
    void should_save_fail_when_user_is_null() {


        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    // throws an exception due duplicate entry .
                    repository.save(null);
                });

        String expectedMessage = USER_INFO_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_email_succeed() {

        User user = new User();
        user.setEmail(nextUniqueEmail());

        Long savedUser = repository.save(user);

        Long updatedUserId = repository.updateEmail(user.getId(), "myNewEmail@mail.be");
        // OPTIONAL
        assertNotNull(updatedUserId);
        // MUST
        assertTrue(updatedUserId > 0);

    }


    @Test
    void should_findById_fail_when_id_is_null() {


        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.findById(null);
                });

        String expectedMessage = USER_ID_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_find_by_id_succeed() {


        User user = new User();
        user.setEmail(nextUniqueEmail());


        User savedUser = UserUtils.saveUserToDatabase(user);
        Optional<User> oId = repository.findById(savedUser.getId());


        assertNotNull(oId);
        assertTrue(savedUser != null);

    }

    @Test
    void should_find_by_id_fail_when_id_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.findById(null);
                });

        String expectedMessage = USER_ID_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_find_all_succeed() {

        User user = new User();
        user.setFirstName("Nil");
        user.setLastName("Molla");
        user.setEmail(nextUniqueEmail());
        user.setPasscode("17689");
        user.setPhone(nextUniquePhoneNumber());

        User savedUser = UserUtils.saveUserToDatabase(user);

        User user2 = new User();
        user2.setFirstName("Nilla");
        user2.setLastName("Mollaca");
        user2.setEmail(nextUniqueEmail());
        user2.setPasscode("17897689");
        user2.setPhone(nextUniquePhoneNumber());
        User savedUser2 = UserUtils.saveUserToDatabase(user2);
        List<User> userList = new ArrayList<User>();
        userList.add(user);
        userList.add(user2);

        List<User> foundUserList = repository.findAll();


        assertArrayEquals(userList.toArray(), foundUserList.toArray());
    }

    @Test
    void find_by_id_fail_when_id_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.findById(null);
                });

        String expectedMessage = USER_ID_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_find_by_id_fail_when_user_id_is_smaller_than_null() {


        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.findById(-1L);
                });

        String expectedMessage = USER_ID_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));


    }

    @Test
    void should_search_with_keyword_succeed() {

        User user = new User();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhoneNumber());
        user.setPasscode("1234");

        User user2 = new User();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail(nextUniqueEmail());
        user2.setPhone(nextUniquePhoneNumber());
        user2.setPasscode("5463");

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user2);
        List<User> searchUserList = repository.search("Justin", "Bieber", "mail@intec");
        assertNotNull(searchUserList);
        assertTrue(searchUserList != null);
    }

    @Test
    void should_findByEmail_succeed() {

        User user = new User();
        user.setFirstName("Rahime");
        user.setLastName("Yildiz");
        user.setEmail(nextUniqueEmail());
        user.setPasscode("4324");
        user.setPhone(nextUniquePhoneNumber());


        User savedUser = UserUtils.saveUserToDatabase(user);
        User foundUserEmail = repository.findByEmail(savedUser.getEmail());


        assertNotNull(foundUserEmail);
        assertTrue(foundUserEmail != null);
    }

    @Test
    void should_findByPhone_succeed() {

        User user = new User();
        user.setFirstName("Ibrahim");
        user.setLastName("Yildiz");
        user.setEmail(nextUniqueEmail());
        user.setPasscode("32456");
        user.setPhone(nextUniquePhoneNumber());


        User savedUser = UserUtils.saveUserToDatabase(user);
        User foundUserPhone = repository.findByPhone(savedUser.getPhone());


        assertNotNull(foundUserPhone);
        assertTrue(foundUserPhone != null);
    }


    @Test
    void should_delete_id_succeed() {

        User user = new User();
        user.setFirstName("John ");
        user.setLastName("Dash");
        user.setPasscode("98387");
        user.setPhone(nextUniquePhoneNumber());
        user.setEmail(nextUniqueEmail());

        User savedUser = UserUtils.saveUserToDatabase(user);
        Long deletedUserId = repository.deleteById(savedUser.getId());
        //optional
        assertNotNull(deletedUserId);
        //must
        assertTrue(deletedUserId > 0);

    }
}