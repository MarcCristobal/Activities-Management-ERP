/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.project2.ERP.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author wilso
 */
@Data
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String place;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private boolean isFree;

    private boolean isLimited;

    @Column(length = 10)
    private int participantLimit;

    @Column(length = 10)
    private double pricePerPerson;

    @Column(length = 10)
    private int numberOfPayments;

    @ElementCollection
    @CollectionTable(name = "activity_resources", joinColumns = @JoinColumn(name = "activity_id"))
    @Column(name = "resource")
    private List<String> resources = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "activity_resources", joinColumns = @JoinColumn(name = "activity_id"))
    @Column(name = "resource")
    private List<String> requirements = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "monitor_id")
    private User monitor;

    public boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(boolean isFree) {
        this.isFree = isFree;
    }

    public boolean getIsLimited() {
        return isLimited;
    }

    public void setIsLimited(boolean isLimited) {
        this.isLimited = isLimited;
    }

}
