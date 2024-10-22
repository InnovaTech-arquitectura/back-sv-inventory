package com.innovatech.inventory.entity;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "Service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceS {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date initialDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date finalDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String multimedia;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_entrepreneurship", nullable = false)
    private Entrepreneurship entrepreneurship;

    public ServiceS(String name, Double price, Date initialDate, Date finalDate, String description) {
        this.name = name;
        this.price = price;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.description = description;
       
    }
    

}
