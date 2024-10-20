package com.innovatech.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoDTO {
    private Long id;
    private String name;
    private int quantity;
    private double price;
    private double cost;
    private String description;
    
    // Para la imagen del producto (en formato byte[] al devolverla al cliente)
    private byte[] picture;  
}
