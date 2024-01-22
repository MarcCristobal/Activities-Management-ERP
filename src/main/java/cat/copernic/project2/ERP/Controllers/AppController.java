/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package cat.copernic.project2.ERP.Controllers;

import cat.copernic.project2.ERP.domain.User;
import static cat.copernic.project2.ERP.domain.UserRole.ACTIVITIES_COORDINATOR;
import static cat.copernic.project2.ERP.domain.UserRole.ADMIN;
import static cat.copernic.project2.ERP.domain.UserRole.CONCIERGE;
import static cat.copernic.project2.ERP.domain.UserRole.MONITOR;
import cat.copernic.project2.ERP.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Marc
 */
@Controller
public class AppController {

          private final UserService userService;

          @Autowired
          public AppController(UserService userService) {
                    this.userService = userService;
          }

          @GetMapping("/")
          public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
                    if (error != null) {
                              model.addAttribute("error", error);
                    }
                    boolean hasNotUsers = userService.hasUsers();
                    model.addAttribute("hasNotUsers", hasNotUsers);
                    return "login";
          }

          @GetMapping("/home")
          public String postLogin(HttpServletRequest request, Authentication authentication) {
                    // Obtén el usuario actualmente autenticado
                    User user = userService.findPersonByEmail(authentication.getName());

                    // Redirige al usuario a una vista específica basada en su rol
                    switch (user.getRole()) {
                              case ADMIN:
                                        return "redirect:/home/users";
                              case ACTIVITIES_COORDINATOR:
                                        return "redirect:/activities";
                              case MONITOR:
                                        return "redirect:/activities";
                              case CONCIERGE:
                                        return "redirect:/activities";
                              default:
                                        return "redirect:/";
                    }
          }
}
