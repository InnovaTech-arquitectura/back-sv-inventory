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

    @Column(name = "quantity_sold") 
    private int quantitySold;

    @Column(name = "sale_number") 
    private String saleNumber;


    // Constructor
    public Sales(Product product, int quantitySold, String saleNumber) {
        this.product = product;
        this.quantitySold = quantitySold;
        this.saleNumber = saleNumber;
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
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public String getSaleNumber() {
        return saleNumber;
    }    

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }
    
}
