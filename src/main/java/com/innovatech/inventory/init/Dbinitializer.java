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
    private UserRepository userEntityRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private OrderStateRepository orderStateRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Inicialización de emprendimientos
        if (entrepreneurshipRepository.count() == 0) {
            System.out.println("Initializing database with sample Entrepreneurship data...");

            List<Entrepreneurship> entrepreneurshipList = List.of(
                new Entrepreneurship("Tech Solutions", "tech_logo.png", "Tech company providing innovative solutions", "John", "Doe"),
                new Entrepreneurship("Creative Designs", "design_logo.png", "Graphic and web design services", "Jane", "Smith")
            );
            entrepreneurshipRepository.saveAll(entrepreneurshipList);
        }

        // Inicializar productos
        if (productRepository.count() == 0) {
            Entrepreneurship entrepreneurship = entrepreneurshipRepository.findAll().get(0); // Usar el primer emprendimiento disponible
            List<Product> products = List.of(
                new Product("Laptop", 10, 1200.00, 800.00, "High-end laptop", "laptop_image.png", entrepreneurship),
                new Product("Smartphone", 25, 800.00, 500.00, "Latest model smartphone", "smartphone_image.png", entrepreneurship),
                new Product("Tablet", 15, 450.00, 300.00, "Compact tablet", "tablet_image.png", entrepreneurship)
            );
            products = productRepository.saveAll(products); // Guarda los productos y recupera los que tienen IDs asignados

            // Imprimir IDs de los productos guardados
            for (Product product : products) {
                System.out.println("Product saved with ID: " + product.getId());
            }
        }

        // Inicialización de ventas
        if (salesRepository.count() == 0) { // Agregar esta verificación para evitar duplicados
            List<Product> products = productRepository.findAll(); // Obtener los productos inicializados
            if (!products.isEmpty()) {
                // Crear ventas con solo ID y nombre del producto
                List<Sales> sales = List.of(
                    new Sales(products.get(0).getId(), products.get(0).getName(), 5), // 5 unidades del Product 1
                    new Sales(products.get(1).getId(), products.get(1).getName(), 3), // 3 unidades del Product 2
                    new Sales(products.get(2).getId(), products.get(2).getName(), 10) // 10 unidades del Product 3
                );
                salesRepository.saveAll(sales); // Guardar las ventas en la base de datos
            }
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
            State california = stateRepository.findAll().stream()
                .filter(s -> s.getName().equals("California"))
                .findFirst().orElse(null);
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
