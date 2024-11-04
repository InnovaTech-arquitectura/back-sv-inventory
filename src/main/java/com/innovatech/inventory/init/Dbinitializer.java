package com.innovatech.inventory.init;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innovatech.inventory.entity.*;
import com.innovatech.inventory.repository.*;

@Component
public class Dbinitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userEntityRepository;
    
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
        if (entrepreneurshipRepository.count() == 0) {
            System.out.println("Initializing database with sample Entrepreneurship and User data...");

       
            List<Entrepreneurship> entrepreneurshipList = List.of(
                new Entrepreneurship("Tech Solutions", "tech_logo.png", "Tech company providing innovative solutions", "John", "Doe"),
                new Entrepreneurship("Creative Designs", "design_logo.png", "Graphic and web design services", "Jane", "Smith"),
                new Entrepreneurship("Healthy Eats", "healthy_logo.png", "Organic and healthy food products", "Emily", "Davis")
            );

       
            entrepreneurshipRepository.saveAll(entrepreneurshipList);

       
            int idCard = 100000; // Puedes comenzar con un valor base y aumentarlo
            for (Entrepreneurship entrepreneurship : entrepreneurshipList) {
                UserEntity user = new UserEntity();
                user.setIdCard(idCard++); // Incrementar el idCard para cada nuevo usuario
                user.setName(entrepreneurship.getNames() + " " + entrepreneurship.getLastnames());
                user.setEmail(entrepreneurship.getNames().toLowerCase() + "@example.com"); // Cambia esto según tus necesidades
                user.setPassword("password123"); // Cambia esto según tus necesidades

       
                userEntityRepository.save(user);

       
                entrepreneurship.setUserEntity(user); // Asociar el usuario con el emprendimiento
            }

       
            entrepreneurshipRepository.saveAll(entrepreneurshipList);

        }

       // Inicializar productos
        //  

        // Inicialización de ventas
        if (salesRepository.count() == 0) { // Agregar esta verificación para evitar duplicados
            List<Product> products = productRepository.findAll(); // Obtener los productos inicializados
            if (!products.isEmpty()) {
                List<Sales> sales = List.of(
                    new Sales(products.get(0), 5, "INV-001"), // 5 unidades del Product 1
                    new Sales(products.get(1), 3, "INV-002"), // 3 unidades del Product 2
                    new Sales(products.get(2), 10, "INV-003") // 10 unidades del Product 3
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