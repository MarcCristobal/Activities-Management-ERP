/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.project2.ERP.services;

import cat.copernic.project2.ERP.dao.UserDao;
import cat.copernic.project2.ERP.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author wilso
 */
@Service
public class InitDBService {

    private final UserDao userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitDBService(UserDao userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void init(User adminUser) {
        if (userRepository.count() == 0) {
            adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
            userRepository.save(adminUser);
        }
    }
}

