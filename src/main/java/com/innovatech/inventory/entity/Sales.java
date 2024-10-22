package com.innovatech.inventory.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "Sales")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sales {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Relationship with table Order_State
    @ManyToOne
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    private Product product;

    @Column
    private int Quantity_sold;


    // Constructor
    public Sales(Product product, int Quantity_sold) {
        this.product = product;
        this.Quantity_sold = Quantity_sold;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantitySold() {
        return Quantity_sold;
    }

    public void setQuantitySold(int Quantity_sold) {
        this.Quantity_sold = Quantity_sold;
    }
    
}
