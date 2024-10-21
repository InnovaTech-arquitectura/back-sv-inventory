package com.innovatech.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.inventory.entity.OrderServiceEntity;

@Repository
public interface OrderServiceEntityRepository extends JpaRepository<OrderServiceEntity, Long> {
    
}
