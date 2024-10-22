package com.innovatech.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.innovatech.inventory.entity.OrderState;

@Repository
public interface OrderStateRepository extends JpaRepository<OrderState, Long> {
    
}
