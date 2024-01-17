/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.project2.ERP.controllers;

import cat.copernic.project2.ERP.domain.User;
import cat.copernic.project2.ERP.services.UserService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author brandon
 */
@Controller
public class UserController {
          private final UserService userService;
          public UserController(UserService userService){
                    this.userService = userService;
          }
          @GetMapping("/home/users")
          public String showAllPersons(Model model) {
                    List<User> users = userService.getAllUsers();
                    model.addAttribute("users", users);
                    return "users";
          }
          @GetMapping("/home/users/user-form")
          public String showUserForm() {
                    return "userForm";
          }
          @PostMapping("/home/person/update")
          public String updateUser(@ModelAttribute User user) {
                    userService.saveOrUpdatePerson(user);
                    return "redirect:/home/users";
          }

          @GetMapping("/home/person/update/{id}")
          public String showUpdateForm(@PathVariable("id") long id, Model model) {
                    User person = userService.findById(id);
                    model.addAttribute("user", person);
                    return "userForm";
          }

          @PostMapping("/home/person/delete/{id}")
          public String deleteUser(@PathVariable("id") long id) {
                    userService.deleteUser(id);
                    return "redirect:/home/users";
          }
}
