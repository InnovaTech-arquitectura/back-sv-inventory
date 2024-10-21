package com.innovatech.inventory.init;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innovatech.inventory.entity.*;
import com.innovatech.inventory.repository.*;

@Component
public class Dbinitializer implements CommandLineRunner {

    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;

    @Autowired
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
            System.out.println("Initializing database with sample Entrepreneurship data...");
            List<Entrepreneurship> entrepreneurshipList = List.of(
                new Entrepreneurship("Tech Solutions", "tech_logo.png", "Tech company providing innovative solutions", "John", "Doe"),
                new Entrepreneurship("Creative Designs", "design_logo.png", "Graphic and web design services", "Jane", "Smith"),
                new Entrepreneurship("Healthy Eats", "healthy_logo.png", "Organic and healthy food products", "Emily", "Davis")
            );
            entrepreneurshipRepository.saveAll(entrepreneurshipList);
        }

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
