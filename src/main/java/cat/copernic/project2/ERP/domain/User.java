/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.project2.ERP.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 *
 * @author brandon
 */
@Data
@Entity
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @Column(length = 50)
        private String name;
        
        @Column(length = 50)
        private String surname;
        
        @Column(length = 50)
        private String email;
        
        @Column(length = 64)
        private String password;

        @Enumerated(EnumType.STRING)
        @Column(length = 15)
        private UserRole role;
        
        @Column(length = 256)
        private String photoPath;
}
