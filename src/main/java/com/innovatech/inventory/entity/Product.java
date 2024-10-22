package com.innovatech.inventory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double cost;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String multimedia;

   // @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_entrepreneurship", nullable = false)
    private Entrepreneurship entrepreneurship;

    // Constructor adicional que acepta todos los parámetros
    public Product(String name, Integer quantity, Double price, Double cost, String description, String multimedia, Entrepreneurship entrepreneurship) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.cost = cost;
        this.description = description;
        this.multimedia = multimedia;
        this.entrepreneurship = entrepreneurship;
    }

    public Product(String name, Integer quantity, Double price, Double cost, String description) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.cost = cost;
        this.description = description;
    }

    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderProducts;
}
