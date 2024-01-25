 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.security;

import erp.services.LoginAttemptService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author brandon
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

        @Autowired
        private LoginAttemptService loginAttemptService;

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                String username = request.getParameter("username");
                
                if (exception instanceof UserBlockedException) {
                        response.sendRedirect("/?error=" + URLEncoder.encode("Tu cuenta ha sido bloqueada debido a demasiados intentos fallidos de inicio de sesión.", "UTF-8") + "&username=" + (username != null ? URLEncoder.encode(username, "UTF-8") : ""));
                }else if(exception instanceof UsernameNotFoundException) {
                        response.sendRedirect("/?error=" + URLEncoder.encode("Usuario o Contraseña incorrectos.", "UTF-8") + "&username=" + (username != null ? URLEncoder.encode(username, "UTF-8") : ""));
                }else if(exception instanceof BadCredentialsException){
                        loginAttemptService.incrementFailedAttempts(username);
                        response.sendRedirect("/?error=" + URLEncoder.encode("Usuario o Contraseña incorrectos.", "UTF-8") + "&username=" + (username != null ? URLEncoder.encode(username, "UTF-8") : ""));
                }
                else {
                        String errorMessage = exception.getMessage();
                        loginAttemptService.incrementFailedAttempts(username);
                        if (errorMessage == null) {
                                errorMessage = "Authentication failed";
                        }
                        response.sendRedirect("/?error=" + URLEncoder.encode(errorMessage, "UTF-8") + "&username=" + (username != null ? URLEncoder.encode(username, "UTF-8") : ""));
                }
        }

}

