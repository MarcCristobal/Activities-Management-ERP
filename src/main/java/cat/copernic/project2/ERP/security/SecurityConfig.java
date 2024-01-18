/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.project2.ERP.security;

import cat.copernic.project2.ERP.dao.UserDao;
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

/**
 *
 * @author brandon
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {


          private final UserDao userRepository;
          private final CustomAuthenticationFailureHandler authenticationFailureHandler;

          @Autowired
          public SecurityConfig(CustomAuthenticationFailureHandler authenticationFailureHandler, UserDao userRepository) {
                    this.authenticationFailureHandler = authenticationFailureHandler;
                    this.userRepository = userRepository; 
          }

          @Bean
          public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                    http
                            .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/images/**").permitAll()
                            .requestMatchers("/css/**").permitAll()
                            .requestMatchers("/register").permitAll()     
                            .requestMatchers("/").permitAll()
                            .anyRequest().authenticated())
                            .formLogin(login -> login
                            .loginPage(userRepository.count() == 0 ? "/register" : "/")
                            .defaultSuccessUrl("/home")
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

}
