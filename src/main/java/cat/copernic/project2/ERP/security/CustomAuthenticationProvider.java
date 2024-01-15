/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.project2.ERP.security;


import cat.copernic.project2.ERP.dao.UserDao;
import cat.copernic.project2.ERP.domain.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author brandon
 */

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

          private final int MAX_ATTEMPTS = 3;
          private final UserDao userRepository;
          private final PasswordEncoder passwordEncoder;

          @Autowired
          public CustomAuthenticationProvider(UserDao userRepository, PasswordEncoder passwordEncoder) {
                    this.userRepository = userRepository;
                    this.passwordEncoder = passwordEncoder;
          }

          @Override
          public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                    String username = authentication.getName();
                    String password = authentication.getCredentials().toString();
                    if (username.equals("admin@admin")) {
                              // Comprueba si la contraseña es correcta
                              if (password.equals("admin")) {
                                        // Crea un usuario en memoria con la autoridad ROLE_ADMIN
                                        return new UsernamePasswordAuthenticationToken(username, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
                              } else {
                                        throw new BadCredentialsException("Contraseña incorrecta.");
                              }
                    }
                    User user = userRepository.findByEmail(username);
                    if (user == null) {
                              throw new UsernameNotFoundException("Usuario no encontrado");
                    }
                    /*if (!person.isAccountNonLocked()) {
                              throw new UserBlockedException("La cuenta está bloqueada");
                    }
                    if (!passwordEncoder.matches(password, person.getPassword())) {
                              // Incrementa el número de intentos fallidos y guarda el usuario
                              person.setFailedAttempts(person.getFailedAttempts() + 1);
                              userRepository.save(person);

                              // Lanza una excepción personalizada
                              throw new BadCredentialsException("Contraseña incorrecta. Te quedan " + (MAX_ATTEMPTS - person.getFailedAttempts()) + " intentos.");
                    }

                    // Si la autenticación es exitosa, resetea los intentos fallidos
                    person.setFailedAttempts(0);*/
                    userRepository.save(user);

                    return new UsernamePasswordAuthenticationToken(username, password, getAuthorities(user));
          }

          @Override
          public boolean supports(Class<?> authentication) {
                    return authentication.equals(UsernamePasswordAuthenticationToken.class);
          }

          private Collection<? extends GrantedAuthority> getAuthorities(User user) {
                    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
          }
}
