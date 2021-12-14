package be.intec.repository;

import be.intec.exceptions.QueryException;
import be.intec.models.Message;
import be.intec.models.Message;
import be.intec.models.User;
import be.intec.utils.MessageUtils;
import be.intec.utils.MessageUtils;
import be.intec.utils.UserUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static be.intec.exceptions.ExceptionMessages.USER_INFO_REQUIRED;
import static be.intec.exceptions.ExceptionMessagesForMessages.MESSAGE_INFO_REQUIRED;
import static org.junit.jupiter.api.Assertions.*;



class MessageRepositoryTest {


    private static int emailIndex = 0;

    private String nextUniqueEmail() {
        return ("user" + (++emailIndex) + "@mail.be");
    }

    private static int phoneIndex = 0;

    private String nextUniquePhoneNumber() {
        return ("04567" + (++phoneIndex));
    }

    private final MessageRepository repository = new MessageRepository();

    @Test
    void should_save_succeed() {
        Message message = new Message();
        message.setSubject("afwezigheid");
        message.setContent("Ik ben ziek");

        Long savedMessageId = repository.save(message);

        assertNotNull(savedMessageId);
        assertNotEquals(0L, savedMessageId);

    }

    @Test
    void should_update_subject_succeed() {

        Message message = new Message();

        User user1 = new User();
        user1.setFirstName("Nikola");
        user1.setLastName("Tesla");
        user1.setEmail("789765445678@tesla.be");
        user1.setPasscode("1234");
        user1.setPhone("7645678");

        User user2 = new User();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail("098545678@tesla.be");
        user2.setPasscode("1234");
        user2.setPhone("87659");

        User from = UserUtils.saveUserToDatabase(user1);
        User to = UserUtils.saveUserToDatabase(user2);


        message.setFromUserId(from);
        message.setToUserId(to);
        message.setSubject("subject");
        message.setContent("content");

        Message savedMessage = MessageUtils.saveMessageToDatabase(message);


        Long updatedMessageId = repository.updateSubject(savedMessage.getId(), "JPA");
        // OPTIONAL
        assertNotNull(updatedMessageId);
        // MUST
        assertTrue(updatedMessageId > 0);


    }

    @Test
    void should_save_fail_when_message_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    // throws an exception due duplicate entry .
                    repository.save(null);
                });

        String expectedMessage = MESSAGE_INFO_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void updateContent() {
        
        
    }

    @Test
    void should_delete_id_succeed() {


        User user1 = new User();
        user1.setFirstName("Nikola");
        user1.setLastName("Tesla");
        user1.setEmail("789765445678@tesla.be");
        user1.setPasscode("1234");
        user1.setPhone("7645678");

        User user2 = new User();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail("098545678@tesla.be");
        user2.setPasscode("1234");
        user2.setPhone("87659");

        User from = UserUtils.saveUserToDatabase(user1);
        User to = UserUtils.saveUserToDatabase(user2);

        Message message = new Message();

        message.setFromUserId(from);
        message.setToUserId(to);
        message.setSubject("subject");
        message.setContent("content");

        Message savedMessage = MessageUtils.saveMessageToDatabase(message);
        Long deletedMessageId = repository.deleteById(savedMessage.getId());
        //optional
        assertNotNull(deletedMessageId);
        //must
        assertTrue(deletedMessageId > 0);

    }

    @Test
    void should_find_by_id_succeed() {


        Message message = new Message();
        message.setContent("content");


        Message savedMessage = MessageUtils.saveMessageToDatabase(message);
        Optional<Message> oId = repository.findById(savedMessage.getId());


        assertNotNull(oId);
        assertTrue(savedMessage != null);

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

        Message message = new Message();

        message.setFromUserId(savedUser);
        message.setToUserId(savedUser2);
        message.setSubject("subject");
        message.setContent("content");


        Message message2 = new Message();

        message2.setFromUserId(savedUser);
        message2.setToUserId(savedUser2);
        message2.setSubject("subject2");
        message2.setContent("content2");


        List<Message> messageList = new ArrayList<Message>();
        messageList.add(message);
        messageList.add(message2);

        List<Message> foundMessageList = repository.findAll(1L,1L);

        assertNotNull(foundMessageList);
       assertTrue(foundMessageList != null);
    }

    @Test
    void findAllBySender() {
    }

    @Test
    void findAllFromReceiver() {
    }
}