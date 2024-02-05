package erp.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import erp.dao.ActivityDao;
import erp.dao.CustomerDao;
import erp.dao.MessageDao;
import erp.dao.UserDao;
import erp.domain.Activity;
import erp.domain.Customer;
import erp.domain.Message;
import erp.domain.User;
import jakarta.transaction.Transactional;

/**
 *
 * @author brandon
 */
@Service
public class MessagingService {

    @Autowired
    private UserDao userRepository;

    @Autowired
    private CustomerDao customerRepository;

    @Autowired
    private ActivityDao activityRepository;

    @Autowired
    private MessageDao messageRepository;

    @Autowired
    private MailSenderService mailSenderService;


    public void sendMessage(Long senderId, String userRecipients, String customerRecipients, String activityRecipients,String subject, String content) {
        User sender = userRepository.findById(senderId).orElse(null);

            Message message = new Message();
            message.setSender(sender);
            message.setSubject(subject);
            message.setContent(content);
            processIds(message, userRecipients, customerRecipients, activityRecipients);

            messageRepository.save(message);
        
    }
    public void processIds(Message message, String userIDs, String customerIDs, String activityIDs) {
        List<String> userList = userIDs != null ? Arrays.asList(userIDs.split(",")) : new ArrayList<>();
        List<String> customerList = customerIDs != null ? Arrays.asList(customerIDs.split(",")) : new ArrayList<>();
        List<String> activityList = activityIDs != null ? Arrays.asList(activityIDs.split(",")) : new ArrayList<>();
    
        for (String userID : userList) {
            if (!userID.isEmpty()) {
                User user = userRepository.findById(Long.parseLong(userID)).orElse(null);
                if (user != null) {
                    message.getUserRecipients().add(user);
                }
            }
        }
    
        for (String customerID : customerList) {
            if (!customerID.isEmpty()) {
                Customer customer = customerRepository.findById(Long.parseLong(customerID)).orElse(null);
                if (customer != null) {
                    message.getCustomerRecipients().add(customer);
                    mailSenderService.sendEmail(customer.getEmail(), message.getSubject(), message.getContent());
                }
            }
        }
    
        for (String activityID : activityList) {
            if (!activityID.isEmpty()) {
                Activity activity = activityRepository.findById(Long.parseLong(activityID)).orElse(null);
                if (activity != null) {
                    List<Customer> participants = activity.getCustomers();
                    for (Customer participant : participants) {
                        message.getCustomerRecipients().add(participant);
                    }
                }
            }
        }
    }
    
    
    public void replyToMessage(Long senderId, Long originalMessageId, String content) {
        User sender = userRepository.findById(senderId).orElse(null);
        Message originalMessage = messageRepository.findById(originalMessageId).orElse(null);

        Message reply = new Message();
        reply.setSender(sender);
        reply.setUserRecipients(originalMessage.getUserRecipients());
        reply.setCustomerRecipients(originalMessage.getCustomerRecipients());
        reply.setContent(content);
        reply.setReplyTo(originalMessage);

        messageRepository.save(reply);
    }
    public List<Message> getAllMessages() {
        
        return messageRepository.findAll();
    }
    public Message getMessageById(long id) {
        return messageRepository.findById(id).orElse(null);
    }
    @Transactional
    public void deleteMessage(long id){
        messageRepository.deleteById(id);
    }
    public List<Message> findAllUserMessages(Long userId) {
        return messageRepository.findMessagesByUserRecipients(userId);
    }

}

