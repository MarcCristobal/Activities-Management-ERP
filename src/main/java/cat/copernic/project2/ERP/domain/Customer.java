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
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 *
 * @author oscar
 */
@Data
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String surnames;

    @Column(length = 50, unique = true)
    private String email;

    @Column(length = 64)
    private Date birthDate;

    @Column(length = 9)
    private String dni;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    @Column(length = 256)
    private String photoPath;

    @Column(length = 50)
    private String parentName;

    @Column(length = 9)
    private String phone;

    @Column(length = 20)
    private String course;

    @Column(length = 20)
    private List<String> interests;
}
