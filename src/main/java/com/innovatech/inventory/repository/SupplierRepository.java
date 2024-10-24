package com.innovatech.inventory.repository;

import com.innovatech.inventory.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository  extends JpaRepository<Supplier, Long> {

    List<Supplier> findByProductsId(Long productId);
}
