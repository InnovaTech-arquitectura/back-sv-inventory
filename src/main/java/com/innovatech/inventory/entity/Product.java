package com.innovatech.inventory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
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

    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private List<Supplier> suppliers = new ArrayList<>();

    public Product(String name, Integer quantity, Double price, Double cost, String description) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.cost = cost;
        this.description = description;
       // this.multimedia = multimedia;
    }

    

    // Relationship with table Order_Product
    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderProducts;


}