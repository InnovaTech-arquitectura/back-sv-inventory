package com.innovatech.inventory.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "Orders")
@Data
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String sale_number;

    @Column
    private String additional_info;

    @Column
    private String address;

    // Relationship with table Order_State
    @ManyToOne
    @JoinColumn(name = "id_state", referencedColumnName = "id")
    private OrderState orderState;

    // Relationship with table Order_Product
    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts;

    // Relationship with table City
    @ManyToOne
    @JoinColumn(name = "id_city", referencedColumnName = "id")
    private City city;

    // Relationship with table Service
    // TODO: Service entity is not defined yet
    // @ManyToMany
    // @JoinTable(
    //     name = "Order_Product", // Name of the intermediate table
    //     joinColumns = @JoinColumn(name = "id_order"), // Foreign key to Order
    //     inverseJoinColumns = @JoinColumn(name = "id_product") // Foreign key to Product
    // )
    // private List<Service> services;
}
