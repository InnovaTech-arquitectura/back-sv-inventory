package com.innovatech.inventory.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders() {

        try{
            return ResponseEntity.ok("All orders");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
    
    
}
