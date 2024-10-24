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

    @Column
    private String sale_number;


    // Constructor
    public Sales(Product product, int Quantity_sold, String sale_number) {
        this.product = product;
        this.Quantity_sold = Quantity_sold;
        this.sale_number = sale_number;
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

    public String getSaleNumber() {
        return sale_number;
    }    

    public void setSaleNumber(String sale_number) {
        this.sale_number = sale_number;
    }
    
}
