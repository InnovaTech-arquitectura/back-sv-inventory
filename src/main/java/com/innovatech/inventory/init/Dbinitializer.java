package com.innovatech.inventory.init;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import com.innovatech.inventory.entity.Entrepreneurship;
import com.innovatech.inventory.entity.UserEntity;
import com.innovatech.inventory.repository.EntrepreneurshipRepository;
import com.innovatech.inventory.repository.UserRepository;

import com.innovatech.inventory.entity.*;
import com.innovatech.inventory.repository.*;


@Component
public class Dbinitializer implements CommandLineRunner {

    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;

    @Autowired

    private UserRepository userEntityRepository;

    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private OrderStateRepository orderStateRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SalesRepository salesRepository; // Asegúrate de que este sea el nombre correcto


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Inicialización de emprendimientos
        if (entrepreneurshipRepository.count() == 0) {

            System.out.println("Initializing database with sample Entrepreneurship and User data...");

            // Crear una lista de emprendimientos

            List<Entrepreneurship> entrepreneurshipList = List.of(
                new Entrepreneurship("Tech Solutions", "tech_logo.png", "Tech company providing innovative solutions", "John", "Doe"),
                new Entrepreneurship("Creative Designs", "design_logo.png", "Graphic and web design services", "Jane", "Smith"),
                new Entrepreneurship("Healthy Eats", "healthy_logo.png", "Organic and healthy food products", "Emily", "Davis")
            );
            entrepreneurshipRepository.saveAll(entrepreneurshipList);
        }

            // Crear y asociar usuarios a los emprendimientos
            int idCard = 100000; // Puedes comenzar con un valor base y aumentarlo
            for (Entrepreneurship entrepreneurship : entrepreneurshipList) {
                UserEntity user = new UserEntity();
                user.setIdCard(idCard++); // Incrementar el idCard para cada nuevo usuario
                user.setName(entrepreneurship.getNames() + " " + entrepreneurship.getLastnames());
                user.setEmail(entrepreneurship.getNames().toLowerCase() + "@example.com"); // Cambia esto según tus necesidades
                user.setPassword("password123"); // Cambia esto según tus necesidades

                // Guardar el usuario y obtener el ID
                userEntityRepository.save(user);

                // Actualizar el emprendimiento con el usuario correspondiente
                entrepreneurship.setUserEntity(user); // Asociar el usuario con el emprendimiento
            }

            // Guardar los emprendimientos con las asociaciones de usuario
            entrepreneurshipRepository.saveAll(entrepreneurshipList);

            System.out.println("Entrepreneurships and Users have been initialized");
        } else {
            System.out.println("Entrepreneurship data already exists");

        List<Product> products = new ArrayList<>();
        
        // Inicializar productos
        if (productRepository.count() == 0) {
            Entrepreneurship entrepreneurship = entrepreneurshipRepository.findAll().get(0); // Usar el primer emprendimiento disponible
            products = List.of(
                new Product("Laptop", 10, 1200.00, 800.00, "High-end laptop", "laptop_image.png", entrepreneurship),
                new Product("Smartphone", 25, 800.00, 500.00, "Latest model smartphone", "smartphone_image.png", entrepreneurship),
                new Product("Tablet", 15, 450.00, 300.00, "Compact tablet", "tablet_image.png", entrepreneurship)
            );
            productRepository.saveAll(products);
        }
        // Inicialización de ventas
        if (salesRepository.count() == 0) { // Agregar esta verificación para evitar duplicados
            List<Sales> sales = List.of(
                new Sales(products.get(0), 5), // 5 unidades del Product 1
                new Sales(products.get(1), 3), // 3 unidades del Product 2
                new Sales(products.get(2), 10) // 10 unidades del Product 3
            );

            salesRepository.saveAll(sales);
        }

        // Inicializar estados
        if (stateRepository.count() == 0) {
            List<State> states = List.of(
                State.builder().name("California").build(),
                State.builder().name("Texas").build(),
                State.builder().name("New York").build()
            );
            stateRepository.saveAll(states);
        }

        // Inicializar ciudades
        if (cityRepository.count() == 0) {
            State california = stateRepository.findAll().stream().filter(s -> s.getName().equals("California")).findFirst().orElse(null);
            if (california != null) {
                List<City> cities = List.of(
                    City.builder().name("Los Angeles").state(california).build(),
                    City.builder().name("San Francisco").state(california).build()
                );
                cityRepository.saveAll(cities);
            }
        }

        // Inicializar estados de la orden
        if (orderStateRepository.count() == 0) {
            List<OrderState> orderStates = List.of(
                OrderState.builder().state("Pending").build(),
                OrderState.builder().state("Shipped").build(),
                OrderState.builder().state("Delivered").build(),
                OrderState.builder().state("Cancelled").build()
            );
            orderStateRepository.saveAll(orderStates);
        }

        System.out.println("Database has been initialized with sample data");
    }
}
