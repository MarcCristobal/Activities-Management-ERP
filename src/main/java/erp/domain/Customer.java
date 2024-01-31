/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.domain;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
          @CsvBindByName(column = "Nom")
          private String name;

          @CsvBindByName(column = "Cognoms")
          @Column(length = 50)
          private String surnames;

          @CsvBindByName(column = "Adreça electrònica")
          @Column(length = 50, unique = true)
          private String email;

          @DateTimeFormat(pattern = "yyyy-MM-dd")
          @CsvCustomBindByName(column = "Data de naixement", converter = MyDateConverter.class)
          private Date birthDate;

          @CsvBindByName(column = "DNI")
          @Column(length = 9, unique = true)
          private String dni;

          @CsvCustomBindByName(column = "Ets", converter = MyEnumConverter.class)
          @Enumerated(EnumType.STRING)
          private CustomerType type;

          @Column(length = 256)
          private String photoPath;

          @CsvBindByName(column = "Nom del pare/mare/tutor legal")
          @Column(length = 50)
          private String parentName;

          @CsvBindByName(column = "Telèfon")
          @Column(length = 9)
          private String phone;

          @CsvBindByName(column = "Curs (Només per estudiants)")
          @Column(length = 20)
          private String course;

          @CsvBindByName(column = "Interessos")
          @Column(length = 256)
          private String interests;

          @ManyToMany
          @JoinTable(
                  name = "customer_activity",
                  joinColumns = @JoinColumn(name = "customer_id"),
                  inverseJoinColumns = @JoinColumn(name = "activity_id"))
          private List<Activity> activities;

          @CsvBindByName(column = "Activitat en què et vols inscriure")
          private String activityNamesString;


}
