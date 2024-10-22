package com.innovatech.inventory.controller;

import com.innovatech.inventory.dto.NewSupplierDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.innovatech.inventory.service.SupplierService;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @GetMapping("/all")
    public ResponseEntity<?> listSuppliers() {
        try {
            return ResponseEntity.ok(supplierService.listSuppliers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error listing suppliers");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(supplierService.getSupplierById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Supplier not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> createSupplier(@RequestBody NewSupplierDTO newSupplierDTO) {
        try {
            return ResponseEntity.ok(supplierService.createSupplier(newSupplierDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating supplier");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editSupplier(@PathVariable Long id, @RequestBody NewSupplierDTO newSupplierDTO) {
        try {
            if (supplierService.getSupplierById(id) == null) {
                return ResponseEntity.badRequest().body("Supplier not found");
            }
            return ResponseEntity.ok(supplierService.editSupplier(id, newSupplierDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error editing supplier");
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getSuppliersByProductId(@PathVariable Long productId) {
        try {
            return ResponseEntity.ok(supplierService.getSuppliersByProductId(productId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error listing suppliers");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.ok("Supplier deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting supplier");
        }
    }

}
