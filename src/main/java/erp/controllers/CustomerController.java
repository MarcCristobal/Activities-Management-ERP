/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package erp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Marc
 */
@Controller
public class CustomerController {

    @GetMapping("/home/customers")
    public String showAllCustomers(Model model) {
        // List<User> customers = userService.getAllUsers();
        //model.addAttribute("customers", customers);
        return "customers";
    }

    @GetMapping("/home/customers/show-participant-list")
    public String showParticipants(Model model) {
        // List<User> customers = userService.getAllUsers();
        //model.addAttribute("customers", customers);
        return "participantList";
    }
}
