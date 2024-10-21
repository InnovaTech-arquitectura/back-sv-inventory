package com.innovatech.inventory.dto;

import java.util.List;
import java.util.Map;

public class NewOrderDTO {
    private String saleNumber;
    private String additionalInfo;
    private String address;
    private Long cityId;
    private Long stateId;
    private Long orderStateId;
    private List<Long> productIds;
    private Map<Long, Integer> productQuantities; // Key: Product ID, Value: Quantity
    private List<Long> serviceIds;

    // Getters and Setters
    public String getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getOrderStateId() {
        return orderStateId;
    }

    public void setOrderStateId(Long orderStateId) {
        this.orderStateId = orderStateId;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public Map<Long, Integer> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(Map<Long, Integer> productQuantities) {
        this.productQuantities = productQuantities;
    }

    public List<Long> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Long> serviceIds) {
        this.serviceIds = serviceIds;
    }
}
