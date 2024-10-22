package com.innovatech.inventory.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innovatech.inventory.entity.Entrepreneurship;
import com.innovatech.inventory.entity.UserEntity;
import com.innovatech.inventory.repository.EntrepreneurshipRepository;
import com.innovatech.inventory.repository.UserRepository;

@Component
public class Dbinitializer implements CommandLineRunner {

    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;

    @Autowired
    private UserRepository userEntityRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Verificar si la base de datos ya contiene emprendimientos
        if (entrepreneurshipRepository.count() == 0) {
            System.out.println("Initializing database with sample Entrepreneurship and User data...");

            // Crear una lista de emprendimientos
            List<Entrepreneurship> entrepreneurshipList = List.of(
                new Entrepreneurship("Tech Solutions", "tech_logo.png", "Tech company providing innovative solutions", "John", "Doe"),
                new Entrepreneurship("Creative Designs", "design_logo.png", "Graphic and web design services", "Jane", "Smith"),
                new Entrepreneurship("Healthy Eats", "healthy_logo.png", "Organic and healthy food products", "Emily", "Davis")
            );

            // Guardar los emprendimientos en la base de datos
            entrepreneurshipRepository.saveAll(entrepreneurshipList);

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
        }
    }
}
