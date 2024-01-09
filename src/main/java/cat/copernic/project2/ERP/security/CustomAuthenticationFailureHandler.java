/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.project2.ERP.security;


import cat.copernic.project2.ERP.services.LoginAttemptService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
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
                    loginAttemptService.incrementFailedAttempts(username);
                    if (exception instanceof UserBlockedException) {
                              response.sendRedirect("/index?error=" + URLEncoder.encode("Tu cuenta ha sido bloqueada debido a demasiados intentos fallidos de inicio de sesión.", "UTF-8") + "&username=" + URLEncoder.encode(username, "UTF-8"));
                    } else {
                              // Usa el mensaje de error de la excepción
                              response.sendRedirect("/index?error=" + URLEncoder.encode(exception.getMessage(), "UTF-8") + "&username=" + URLEncoder.encode(username, "UTF-8"));
                    }
          }

}
