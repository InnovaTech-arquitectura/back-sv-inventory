package com.innovatech.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.innovatech.inventory.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    
}
