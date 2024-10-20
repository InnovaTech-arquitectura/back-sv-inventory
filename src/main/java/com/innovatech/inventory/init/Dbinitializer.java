package com.innovatech.inventory.init;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innovatech.inventory.entity.Product;
import com.innovatech.inventory.repository.ProductRepository;

@Component
public class Dbinitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Products have been initialized");
    }
    
}
