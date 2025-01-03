package com.innovatech.inventory.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.inventory.dto.ProductInfoDTO;
import com.innovatech.inventory.dto.ServiceDTO;
import com.innovatech.inventory.dto.ServiceInfoDTO;
import com.innovatech.inventory.entity.Product;
import com.innovatech.inventory.entity.ServiceS;
import com.innovatech.inventory.service.MinioService;
import com.innovatech.inventory.service.ServiceService;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private MinioService minioService;

 private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/all")
    public ResponseEntity<?> listServices(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            Page<ServiceInfoDTO> servicesDTOPage = serviceService.listServices(pageable);
            return ResponseEntity.ok(servicesDTOPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.TEXT_PLAIN)
                                .body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findService(@PathVariable Long id) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException {
        try {
            ServiceS foundService = serviceService.findService(id);
            return ResponseEntity.ok(new ServiceInfoDTO(foundService.getId(), foundService.getName(), foundService.getPrice(), foundService.getInitialDate(), foundService.getFinalDate(), foundService.getDescription(), IOUtils.toByteArray(minioService.getObject(foundService.getMultimedia()))));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editService(@PathVariable Long id, @ModelAttribute ServiceDTO editedServiceDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException, ParseException {
        try {
            ServiceS editedService = serviceService.editService(id, editedServiceDto);

            try {
                minioService.uploadFile("s-" + editedService.getId().toString(), editedServiceDto.getPicture());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Error uploading photo");
            }

            return ResponseEntity.ok(editedService);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is already a service with the same name");
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> createService(@ModelAttribute ServiceDTO newServiceDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException, ParseException {
        logger.info("Received request to create a new service with name: {}", newServiceDto.getName());
    
        try {
            // Crear el servicio
            ServiceS newService = serviceService.createService(newServiceDto);
            logger.info("Service created successfully with ID: {}", newService.getId());
    
            // Subir la imagen del servicio a MinIO
            try {
                minioService.uploadFile("s-" + newService.getId().toString(), newServiceDto.getPicture());
                logger.info("Image uploaded successfully for service with ID: {}", newService.getId());
            } catch (IOException e) {
                logger.error("Error uploading image for service with ID: {}", newService.getId(), e);
                serviceService.deleteService(newService.getId());
                throw new RuntimeException(e);
            } catch (Exception e) {
                logger.error("Error during file upload for service with ID: {}", newService.getId(), e);
                serviceService.deleteService(newService.getId());
                return ResponseEntity.badRequest().body("Error uploading photo");
            }
    
            // Respuesta exitosa con el nuevo servicio
            return ResponseEntity.ok(newService);
        } catch (DataIntegrityViolationException e) {
            logger.error("Service with name '{}' already exists", newServiceDto.getName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is already a service with the same name");
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        try {
            serviceService.deleteService(id);

            try {
                minioService.deleteFile("s-" + id.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error deleting photo");
            }

            return ResponseEntity.ok("Service deleted");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }
    }

    @GetMapping("/entrepreneurship/{id_user_entity}")
    public ResponseEntity<?> getServicesByEntrepreneurship(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page,
            @PathVariable("id_user_entity") Long userId) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            Page<ServiceInfoDTO> servicesDTOPage = serviceService.getServicesByEntrepreneurshipId(userId, pageable);
            return ResponseEntity.ok(servicesDTOPage);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Entrepreneurship not found for user ID: " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching services for user ID: " + userId + ". Details: " + e.getMessage());
        }
        
    }
}