/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.security;

import erp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 *
 * @author brandon
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final CustomAuthenticationFailureHandler authenticationFailureHandler;
        private final UserService userService;

        @Autowired
        public SecurityConfig(CustomAuthenticationFailureHandler authenticationFailureHandler,
                        UserService userService) {
                this.authenticationFailureHandler = authenticationFailureHandler;
                this.userService = userService;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/activities-board").permitAll()
                                                .requestMatchers("/activities-board/**").permitAll()
                                                .requestMatchers("/images/**").permitAll()
                                                .requestMatchers("/css/**").permitAll()
                                                .requestMatchers(new RegisterRequestMatcher()).permitAll()
                                                .requestMatchers("/").permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(login -> login
                                                .loginPage("/")
                                                .defaultSuccessUrl("/home", true)
                                                .failureHandler(authenticationFailureHandler))
                                .logout(logout -> logout
                                                .logoutUrl("/my/ownlogout") // Custom logout URL
                                                .logoutSuccessUrl("/")); // Redirect to custom login page after logout
                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationEntryPoint authenticationEntryPoint() {
                BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
                entryPoint.setRealmName("admin realm");
                return entryPoint;
        }

        private class RegisterRequestMatcher implements RequestMatcher {

                @Override
                public boolean matches(HttpServletRequest request) {
                        // Solo permite el acceso a /register si no hay usuarios
                        return request.getServletPath().equals("/register") && userService.hasUsers();
                }

        }

}
