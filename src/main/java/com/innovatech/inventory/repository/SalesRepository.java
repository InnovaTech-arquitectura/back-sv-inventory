package com.innovatech.inventory.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.innovatech.inventory.entity.Sales;

import org.springframework.data.domain.Pageable;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    
    @Query("SELECT s FROM Sales s WHERE s.product.entrepreneurship.id = :entrepreneurshipId")
    Page<Sales> findByEntrepreneurshipId(@Param("entrepreneurshipId") Long entrepreneurshipId, Pageable pageable);

}