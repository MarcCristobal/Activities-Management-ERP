/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.services;

import erp.dao.UserDao;
import erp.domain.User;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author wilso
 */
@Service
public class LoginAttemptService {
    private final int MAX_ATTEMPT = 3;
    private final UserDao userDao;

    @Autowired
    public LoginAttemptService(UserDao personDao){
        this.userDao = personDao;
    }

    public void incrementFailedAttempts(String username){
        User user = userDao.findByEmail(username);
        user.setFailedAttempts(user.getFailedAttempts()+1);
        if (user.getFailedAttempts() >= MAX_ATTEMPT) {
            user.setAccountNonLocked(false);
            user.setLockTime(LocalDateTime.now());// Bloquea la cuenta del usuario
        }
        userDao.save(user); // Guarda los cambios en la base de datos*/
    }

    public void resetFailedAttempts(String username){
        User person = userDao.findByEmail(username);
        person.setFailedAttempts(0);
        person.setAccountNonLocked(true); // Desbloquea la cuenta del usuario
        userDao.save(person); // Guarda los cambios en la base de datos
    }

    public boolean isUserBlocked(String username) {
        User user = userDao.findByEmail(username);
        return !user.isAccountNonLocked();
    }
}
