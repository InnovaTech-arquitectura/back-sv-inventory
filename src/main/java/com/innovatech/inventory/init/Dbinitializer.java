package com.innovatech.inventory.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innovatech.inventory.entity.Entrepreneurship;
import com.innovatech.inventory.repository.EntrepreneurshipRepository;

@Component
public class Dbinitializer implements CommandLineRunner {

    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Verificar si la base de datos ya contiene emprendimientos
        if (entrepreneurshipRepository.count() == 0) {
            System.out.println("Initializing database with sample Entrepreneurship data...");

            // Crear una lista de emprendimientos
            List<Entrepreneurship> entrepreneurshipList = List.of(
                new Entrepreneurship("Tech Solutions", "tech_logo.png", "Tech company providing innovative solutions", "John", "Doe"),
                new Entrepreneurship("Creative Designs", "design_logo.png", "Graphic and web design services", "Jane", "Smith"),
                new Entrepreneurship("Healthy Eats", "healthy_logo.png", "Organic and healthy food products", "Emily", "Davis")
            );

            // Guardar los emprendimientos en la base de datos
            entrepreneurshipRepository.saveAll(entrepreneurshipList);

            System.out.println("Entrepreneurships have been initialized");
        } else {
            System.out.println("Entrepreneurship data already exists");
        }
    }
}
