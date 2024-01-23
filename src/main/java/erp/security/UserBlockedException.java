/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.security;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author brandon
 */
public class UserBlockedException extends AuthenticationException {

    public UserBlockedException(String message) {
        super(message);
    }
}

