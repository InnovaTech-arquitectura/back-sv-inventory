package com.innovatech.inventory.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.inventory.entity.Entrepreneurship;
@Repository
public interface EntrepreneurshipRepository extends JpaRepository<Entrepreneurship, Long> {
	
}
