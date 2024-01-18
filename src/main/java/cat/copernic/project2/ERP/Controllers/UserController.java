/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.project2.ERP.controllers;

import cat.copernic.project2.ERP.domain.User;
import cat.copernic.project2.ERP.security.CustomAuthenticationProvider;
import cat.copernic.project2.ERP.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author brandon
 */
@Controller
public class UserController {

          private final UserService userService;
          private final CustomAuthenticationProvider authenticationProvider;
          private final AuthenticationManagerBuilder authenticationManagerBuilder;

          public UserController(UserService userService, CustomAuthenticationProvider authenticationProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
                    this.userService = userService;
                    this.authenticationProvider = authenticationProvider;
                    this.authenticationManagerBuilder = authenticationManagerBuilder;
          }

          @GetMapping("/register")
          public String showRegistrationForm(Model model) {
                    if (!model.containsAttribute("user")) {
                              model.addAttribute("user", new User());
                    }
                    return "register";
          }

          @PostMapping("/register")
          public String register(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes) {
                    if (result.hasErrors()) {
                              redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
                              redirectAttributes.addFlashAttribute("user", user);
                              return "redirect:/register";
                    }
                    // Guarda el usuario en la base de datos...
                    userService.saveOrUpdatePerson(user);
                    return "login";
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

          @GetMapping("/home/profile/{id}")
          public String showUserProfile(@PathVariable("id") Long id, Model model) {
                    User user = userService.findById(id);
                    model.addAttribute("user", user);
                    return "userProfile";
          }

}
