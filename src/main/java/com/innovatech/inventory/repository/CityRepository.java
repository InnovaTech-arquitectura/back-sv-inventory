package com.innovatech.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.innovatech.inventory.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    
}
