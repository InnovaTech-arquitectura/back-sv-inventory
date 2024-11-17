package com.innovatech.inventory.dto;

public class SalesRequestDTO {

    private ProductSaleDTO product;  // Información básica del producto (ID y nombre)
    private int quantitySold;


    // Constructor
    public SalesRequestDTO(ProductSaleDTO product, int quantitySold) {
        this.product = product;
        this.quantitySold = quantitySold;
    }

    // Getters y Setters
    public ProductSaleDTO getProduct() {
        return product;
    }

    public void setProduct(ProductSaleDTO product) {
        this.product = product;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

}
