package com.innovatech.inventory.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String name;
    private Long idEntrepreneurship;
    private int quantity;
    private double price;
    private double cost;
    private String description;
    
    // Para la imagen del producto (subida desde el cliente)
    private MultipartFile picture;  
}
