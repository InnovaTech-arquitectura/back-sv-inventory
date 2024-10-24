package com.innovatech.inventory.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.inventory.entity.UserEntity;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
}
