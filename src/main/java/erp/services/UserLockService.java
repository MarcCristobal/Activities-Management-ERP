/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.services;

import erp.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

/**
 *
 * @author brandon
 */
@Service
public class UserLockService {

    private final UserService userService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public UserLockService(UserService userService) {
        this.userService = userService;
        this.scheduler.scheduleAtFixedRate(this::unlockUsers, 0, 1, TimeUnit.MINUTES);
    }

    private void unlockUsers() {
        List<User> lockedUsers = userService.getLockedUsers();
        
        for (User user : lockedUsers) {
            if (user.getLockTime().isBefore(LocalDateTime.now().minusMinutes(1))) {
                    user.setFailedAttempts(0);
                user.setAccountNonLocked(true);
                userService.unLockUser(user);
            }
        }
    }
}