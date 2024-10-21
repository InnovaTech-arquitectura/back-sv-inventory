package com.innovatech.inventory.init;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innovatech.inventory.entity.Product;
import com.innovatech.inventory.entity.Sales;
import com.innovatech.inventory.repository.ProductRepository;
import com.innovatech.inventory.repository.SalesRepository;

@Component
public class Dbinitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SalesRepository saleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Product 1", 10, 100.0, 50.0, "Description of Product 1", "p-1"));
        products.add(new Product("Product 2", 20, 200.0, 100.0, "Description of Product 2", "p-2"));
        products.add(new Product("Product 3", 30, 300.0, 150.0, "Description of Product 3", "p-3"));
        products.add(new Product("Product 4", 40, 400.0, 200.0, "Description of Product 4", "p-4"));
        products.add(new Product("Product 5", 50, 500.0, 250.0, "Description of Product 5", "p-5"));

        for (Product product : products) {
            productRepository.save(product);
        }

        // Inicialización de ventas
        List<Sales> sales = new ArrayList<>();
        sales.add(new Sales(products.get(0), 5)); // 5 unidades del Product 1
        sales.add(new Sales(products.get(1), 3)); // 3 unidades del Product 2
        sales.add(new Sales(products.get(2), 10)); // 10 unidades del Product 3

        for (Sales sale : sales) {
            saleRepository.save(sale);
        }

        System.out.println("Products and Sales have been initialized");
    }
}
