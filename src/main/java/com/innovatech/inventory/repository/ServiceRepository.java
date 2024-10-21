package com.innovatech.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.innovatech.inventory.entity.ServiceS;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceS, Long> {
    boolean existsByName(String name);
       Optional<ServiceS> findByName(String name);
}
