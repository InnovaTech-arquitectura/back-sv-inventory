package com.innovatech.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceInfoDTO {
    private Long id;
    private String name;
    private double price;
    private Date initialDate;
    private Date finalDate;
    private String description;

    // Para la imagen del servicio (en formato byte[] al devolverla al cliente)
    private byte[] picture;
}
