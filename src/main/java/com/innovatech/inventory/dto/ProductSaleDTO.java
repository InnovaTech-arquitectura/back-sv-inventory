package com.innovatech.inventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductSaleDTO {

    private Long id;
    private String name;

    // Constructor
    public ProductSaleDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
