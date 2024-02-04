/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import erp.domain.Message;
import erp.domain.User;
import erp.services.MessagingService;
import erp.services.UserService;


/**
 *
 * @author brandon
 */
@Controller
public class MessagingController {
        
        @Autowired
        private final MessagingService messagingService;

        @Autowired
        private final UserService userService;
        
        public MessagingController(MessagingService messagingService, UserService userService){
                this.messagingService = new MessagingService();
                this.userService = userService;

        }
        @GetMapping("/home/communications")
        public String showMessageList(){
                return "messageList";
        }
        @GetMapping("/home/communications/message-form")
        public String createMessage (Model model) {
                Message message = new Message();
                List<User> users = userService.getAllUsers();
                List<User> userRecipent = message.getUserRecipients();
                model.addAttribute("userRecipients", userRecipent);
                model.addAttribute("message", message);
                model.addAttribute("users", users);
                return "messageForm";
        }
        
}
