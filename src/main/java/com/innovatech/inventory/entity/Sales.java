package com.innovatech.inventory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.innovatech.inventory.dto.ProductSaleDTO;
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

    @ManyToOne(fetch = FetchType.LAZY)  // Relación con Product
    @JsonIgnore
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    private Product product;

    @Column(name = "quantity_sold")
    private int quantitySold;

    // Método para obtener el DTO del producto con solo id y name
    public ProductSaleDTO getProductSaleDTO() {
        if (product != null) {
            return new ProductSaleDTO(product.getId(), product.getName());
        }
        return null;
    }

    // Constructor modificado para aceptar id y nombre del producto
    public Sales(long productId, String productName, int quantitySold) {
        this.product = new Product();  // o cargar el producto por ID
        this.product.setId(productId);
        this.product.setName(productName);
        this.quantitySold = quantitySold;

    }
}
