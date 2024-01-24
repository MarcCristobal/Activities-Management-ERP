/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.services;

import java.security.SecureRandom;
import org.springframework.stereotype.Service;

/**
 *
 * @author wilso
 */
@Service
public class PasswordGenerator {

          private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
          private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
          private static final String NUMBER = "0123456789";
          private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";
          private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;

          private static SecureRandom random = new SecureRandom();

          public static String generateRandomPassword(int length) {
                    if (length < 8 || length > 16) {
                              throw new IllegalArgumentException("La longitud de la contraseña debe estar entre 8 y 16 caracteres.");
                    }

                    StringBuilder password = new StringBuilder(length);
                    for (int i = 0; i < length; i++) {
                              password.append(PASSWORD_ALLOW_BASE.charAt(random.nextInt(PASSWORD_ALLOW_BASE.length())));
                    }
                    System.out.println("Contraseña generada: "+password);
                    return password.toString();
          }

}
