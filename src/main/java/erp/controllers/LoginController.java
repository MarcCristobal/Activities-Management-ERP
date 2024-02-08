/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package erp.controllers;

import erp.domain.User;
import static erp.domain.UserRole.ACTIVITIES_COORDINATOR;
import static erp.domain.UserRole.ADMIN;
import static erp.domain.UserRole.CONCIERGE;
import static erp.domain.UserRole.MONITOR;
import erp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Marc
 */
@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
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
        User user = userService.findUserByEmail(authentication.getName());

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
