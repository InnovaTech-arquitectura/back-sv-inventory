package com.innovatech.inventory;

import com.innovatech.inventory.controller.SupplierController;
import com.innovatech.inventory.dto.NewSupplierDTO;
import com.innovatech.inventory.entity.Supplier;
import com.innovatech.inventory.service.SupplierService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupplierControllerTest {

    @InjectMocks
    private SupplierController supplierController;

    @Mock
    private SupplierService supplierService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListSuppliers() {
        // Arrange
        Supplier supplier1 = new Supplier();
        supplier1 = Supplier.builder()
                .id(1L)
                .name("Supplier 1")
                .description("Supplier 1 description")
                .contact_number("1234567890")
                .build();
        Supplier supplier2 = new Supplier();
        supplier2 = Supplier.builder()
                .id(2L)
                .name("Supplier 2")
                .description("Supplier 2 description")
                .contact_number("0987654321")
                .build();

        List<Supplier> suppliers = List.of(supplier1, supplier2);
        Mockito.when(supplierService.listSuppliers()).thenReturn(suppliers);
        // Act
        ResponseEntity<?> response = supplierController.listSuppliers();
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(suppliers, response.getBody());
    }

    @Test
    public void testGetSupplierById() {
        // Arrange
        Supplier supplier1 = new Supplier();
        supplier1 = Supplier.builder()
                .id(1L)
                .name("Supplier 1")
                .description("Supplier 1 description")
                .contact_number("1234567890")
                .build();
        Mockito.when(supplierService.getSupplierById(1L)).thenReturn(supplier1);
        // Act
        ResponseEntity<?> response = supplierController.getSupplierById(1L);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(supplier1, response.getBody());
    }

    @Test
    public void testCreateSupplier() {
        // Arrange
        NewSupplierDTO supplierDTO = new NewSupplierDTO("Supplier 1", "Supplier 1 description", "1234567890", new Long[]{1L, 2L});
        Supplier supplier = Supplier.builder()
                .id(1L)
                .name("Supplier 1")
                .description("Supplier 1 description")
                .contact_number("1234567890")
                .build();
        Mockito.when(supplierService.createSupplier(supplierDTO)).thenReturn(supplier);
        // Act
        ResponseEntity<?> response = supplierController.createSupplier(supplierDTO);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(supplier, response.getBody());
    }

    @Test
    public void testEditSupplier() {
        // Arrange
        NewSupplierDTO supplierDTO = new NewSupplierDTO("Supplier 1", "Supplier 1 description", "1234567890", new Long[]{1L, 2L});
        Supplier supplier = Supplier.builder()
                .id(1L)
                .name("Supplier 1")
                .description("Supplier 1 description")
                .contact_number("1234567890")
                .build();
        Mockito.when(supplierService.getSupplierById(1L)).thenReturn(supplier);
        Mockito.when(supplierService.editSupplier(1L, supplierDTO)).thenReturn(supplier);
        // Act
        ResponseEntity<?> response = supplierController.editSupplier(1L, supplierDTO);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(supplier, response.getBody());
    }

    @Test
    public void testGetSuppliersByProductId() {
        // Arrange
        Supplier supplier1 = new Supplier();
        supplier1 = Supplier.builder()
                .id(1L)
                .name("Supplier 1")
                .description("Supplier 1 description")
                .contact_number("1234567890")
                .build();
        Supplier supplier2 = new Supplier();
        supplier2 = Supplier.builder()
                .id(2L)
                .name("Supplier 2")
                .description("Supplier 2 description")
                .contact_number("0987654321")
                .build();
        // Act
        ResponseEntity<?> response = supplierController.getSuppliersByProductId(1L);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteSupplier() {
        // Arrange
        Supplier supplier1 = new Supplier();
        supplier1 = Supplier.builder()
                .id(1L)
                .name("Supplier 1")
                .description("Supplier 1 description")
                .contact_number("1234567890")
                .build();
        Mockito.when(supplierService.getSupplierById(1L)).thenReturn(supplier1);
        // Act
        ResponseEntity<?> response = supplierController.deleteSupplier(1L);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
