package com.innovatech.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class NewSupplierDTO {
    private String name;
    private String description;
    private String contact_number;
    private Long[] products;
}
