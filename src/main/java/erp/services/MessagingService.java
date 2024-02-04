package erp.services;

import java.util.ArrayList;
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

    public void sendMessage(Long senderId, HashMap<Class<?>, List<String>> recipientsMap, String subject, String content) {
        User sender = userRepository.findById(senderId).orElse(null);

        for (Map.Entry<Class<?>, List<String>> entry : recipientsMap.entrySet()) {
            Class<?> key = entry.getKey();
            List<String> recipientEmails = entry.getValue();

            List<User> userRecipients = new ArrayList<>();
            List<Customer> customerRecipients = new ArrayList<>();

            for (String email : recipientEmails) {
                if (key.equals(User.class)) {
                    User user = userRepository.findByEmail(email);
                    if (user != null) {
                        userRecipients.add(user);
                    }
                } else if (key.equals(Customer.class)) {
                    Customer customer = customerRepository.findByEmail(email);
                    if (customer != null) {
                        customerRecipients.add(customer);
                        mailSenderService.sendEmail(customer.getEmail(), subject, content);
                    }
                } else if (key.equals(Activity.class)) {
                    Activity activity = activityRepository.findById(Long.parseLong(email)).orElse(null);
                    if (activity != null) {
                        for (Customer participant : activity.getCustomers()) {
                            customerRecipients.add(participant);
                            mailSenderService.sendEmail(participant.getEmail(), subject, content);
                        }
                    }
                }
            }

            Message message = new Message();
            message.setSender(sender);
            message.setUserRecipients(userRecipients);
            message.setCustomerRecipients(customerRecipients);
            message.setSubject(subject);
            message.setContent(content);

            messageRepository.save(message);
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
}

