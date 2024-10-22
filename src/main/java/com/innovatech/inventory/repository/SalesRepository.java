package com.innovatech.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.inventory.entity.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    
}
