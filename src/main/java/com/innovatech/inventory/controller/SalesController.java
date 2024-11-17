package com.innovatech.inventory.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.inventory.entity.Product;
import com.innovatech.inventory.entity.Sales;
import com.innovatech.inventory.repository.ProductRepository;
import com.innovatech.inventory.service.SalesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.innovatech.inventory.repository.SalesRepository;


@RestController
@RequestMapping("/sales")
public class SalesController {
    
    
    @Autowired
    private SalesService salesService;

    @Autowired
    private SalesRepository SalesRepository;

    @Autowired
    private ProductRepository productRepository;

     @GetMapping("/all")
    public ResponseEntity<?> getAllSales(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            Page<Sales> salesPage = salesService.findAll(pageable);
            return ResponseEntity.ok(salesPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An unexpected error occurred");
        }
    }

    @GetMapping("/by-entrepreneurship/{entrepreneurshipId}")
    public ResponseEntity<?> getSalesByEntrepreneurship(
            @PathVariable Long entrepreneurshipId, // Variable de ruta
            @RequestParam(defaultValue = "10") int limit, // Parámetro para el tamaño de la página
            @RequestParam(defaultValue = "0") int page // Parámetro para el número de página
    ) {
        try {
            // Crear el objeto de paginación
            Pageable pageable = PageRequest.of(page, limit);

            // Obtener las ventas paginadas desde el repositorio
            Page<Sales> salesPage = SalesRepository.findByEntrepreneurshipId(entrepreneurshipId, pageable);

            // Retornar la respuesta paginada
            return ResponseEntity.ok(salesPage);
        } catch (Exception e) {
            // Manejar errores y devolver un mensaje genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @PostMapping("/add")
    public ResponseEntity<?> addSale(@RequestBody Sales sales) {
        try {
            // Verificar si el producto existe
            Product existingProduct = productRepository.findById(sales.getProduct().getId()).orElse(null);

            if (existingProduct == null) {
                return new ResponseEntity<>("Conflict: Product not found", HttpStatus.NOT_FOUND);
            }

            // Asegurarse de que la cantidad vendida no exceda el stock
            if (sales.getQuantitySold() > existingProduct.getQuantity()) {
                return new ResponseEntity<>("Conflict: Insufficient stock", HttpStatus.CONFLICT);
            }

            // Guardar la nueva venta
            Sales saleDB = salesService.save(sales);

            if (saleDB == null) {
                return new ResponseEntity<>("Unable to add Sale", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(saleDB, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Unable to add Sale", HttpStatus.BAD_REQUEST);
        }
    }


    
}


