package com.innovatech.inventory.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {
    private String name;
    private Long idEntrepreneurship;
    private double price;
    private String initialDate;  // Cambiar a String
    private String finalDate; 
    private String description;

    // Para la imagen del servicio (subida desde el cliente)
    private MultipartFile picture;
}
