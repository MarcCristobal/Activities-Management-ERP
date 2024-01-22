/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.project2.ERP.controllers;

import cat.copernic.project2.ERP.domain.User;
import cat.copernic.project2.ERP.security.CustomAuthenticationProvider;
import cat.copernic.project2.ERP.services.PasswordGenerator;
import cat.copernic.project2.ERP.services.UserService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import cat.copernic.project2.ERP.services.MailSenderService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author brandon
 */
@Controller
public class UserController {

        private final UserService userService;
        private final CustomAuthenticationProvider authenticationProvider;
        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final PasswordGenerator passwordGenerator;
        private final MailSenderService javaMailSender;

        @Autowired
        public UserController(UserService userService, CustomAuthenticationProvider authenticationProvider, AuthenticationManagerBuilder authenticationManagerBuilder, PasswordGenerator passwordGenerator, MailSenderService javaMailSender) {
                this.userService = userService;
                this.authenticationProvider = authenticationProvider;
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.passwordGenerator = passwordGenerator;
                this.javaMailSender = javaMailSender;
        }

        @GetMapping("/register")
        public String showRegistrationForm(User user) {
                return "register";
        }

        @PostMapping("/register")
        public String register(@ModelAttribute User user, @RequestParam("photo") MultipartFile photo) {
                try {
                        String photoPath;
                        if (photo != null && !photo.isEmpty()) {
                                // Si el usuario ha seleccionado una foto, la procesamos y guardamos
                                photoPath = userService.savePhoto(photo);
                        } else {
                                // Si el usuario no ha seleccionado una foto, usamos una imagen predeterminada
                                photoPath = "/images/usuario.png";
                        }

                        // Establecemos la ruta de la foto en el usuario
                        user.setPhotoPath(photoPath);

                        // Guardamos el usuario
                        userService.saveOrUpdateUser(user);
                } catch (IOException e) {
                        // Manejamos la excepción
                        System.out.println(e.getMessage());
                }

                return "redirect:/login";
        }

        @GetMapping("/home/users")
        public String showAllPersons(Model model) {
                List<User> users = userService.getAllUsers();
                model.addAttribute("users", users);
                return "users";
        }

        @GetMapping("/home/users/user-form")
        public String showUserForm( Model model) {
                User user = new User();
                model.addAttribute("user", user);
                return "userForm";
        }

        @PostMapping("/home/users/update")
        public String updateUser(@ModelAttribute User user, @RequestParam("photo") MultipartFile photo) {
                try {
                        String photoPath;
                        if (photo != null && !photo.isEmpty()) {
                                // Si el usuario ha seleccionado una foto, la procesamos y guardamos
                                photoPath = userService.savePhoto(photo);
                        } else {
                                // Si el usuario no ha seleccionado una foto, usamos una imagen predeterminada
                                photoPath = "/images/usuario2.png";
                        }

                        // Establecemos la ruta de la foto en el usuario
                        user.setPhotoPath(photoPath);

                        // Guardamos el usuario
                        user.setPassword(passwordGenerator.generateRandomPassword(8));
                        javaMailSender.sendEmail(user.getEmail(), "Cuenta Activities ERP", "Aqui tiene sus credenciales nuevas y el link para regenerar la contraseña:"+user.getPassword());
                        userService.saveOrUpdateUser(user);
                }
                catch (IOException e) {
                        // Manejamos la excepción
                        System.out.println(e.getMessage());
                }

                return "redirect:/home/users";
        }

        @GetMapping("/home/users/update/{id}")
        public String showUpdateForm(@PathVariable("id") long id, Model model) {
                User person = userService.findById(id);
                model.addAttribute("user", person);
                return "userForm";
        }

        @PostMapping("/home/users/delete/{id}")
        public String deleteUser(@PathVariable("id") long id) {
                userService.deleteUser(id);
                return "redirect:/home/users";
        }

        @GetMapping("/home/users/{id}")
        public String showUserProfile(@PathVariable("id") Long id, Model model) {
                User user = userService.findById(id);
                model.addAttribute("user", user);
                return "userOverview";
        }

}
