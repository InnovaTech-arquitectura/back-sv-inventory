package com.innovatech.inventory.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import com.innovatech.inventory.dto.ProductDTO;
import com.innovatech.inventory.dto.ProductInfoDTO;
import com.innovatech.inventory.entity.Product;
import com.innovatech.inventory.entity.UserEntity;
import com.innovatech.inventory.service.MinioService;
import com.innovatech.inventory.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@RestController
@RequestMapping("/product")
public class ProductController{

    @Autowired
    private ProductService productService;

    @Autowired
    private MinioService minioService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    
    @GetMapping("/all")
    public Page<ProductInfoDTO> listProducts(Pageable pageable)
    throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException
    {
        
        List<Product> products = productService.listProducts();

        List<ProductInfoDTO> productsDTO = new ArrayList<>();
        for (Product product : products) {
            ProductInfoDTO productDTO = new ProductInfoDTO(product.getId(), product.getName(), product.getQuantity(), product.getPrice(), product.getCost(), product.getDescription(), IOUtils.toByteArray(minioService.getObject(product.getMultimedia())));
            productsDTO.add(productDTO);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productsDTO.size());
        List<ProductInfoDTO> paginatedList = productsDTO.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, productsDTO.size());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProduct(@PathVariable Long id) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException {
        try {
            Product foundProduct = productService.findProduct(id);
            return ResponseEntity.ok(new ProductInfoDTO(foundProduct.getId(), foundProduct.getName(), foundProduct.getQuantity(), foundProduct.getPrice(), foundProduct.getCost(), foundProduct.getDescription(), IOUtils.toByteArray(minioService.getObject(foundProduct.getMultimedia()))));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

 

    @PutMapping("/{id}")
    public ResponseEntity<?> editProduct(@PathVariable Long id, @ModelAttribute ProductDTO editedProductDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException {
        if (editedProductDto.getQuantity() < 0) {
            return ResponseEntity.badRequest().body("La cantidad no puede ser negativa");
        }
        if (editedProductDto.getCost() < 0) {
            return ResponseEntity.badRequest().body("El precio del costo no puede ser negativo");
        }
        if (editedProductDto.getPrice() < 0) {
            return ResponseEntity.badRequest().body("El precio no puede ser negativo");
        }
        
        try {
            Product editedProduct = productService.editProduct(id, editedProductDto);

            try {
                minioService.uploadFile("p-" + editedProduct.getId().toString(), editedProductDto.getPicture());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Error uploading photo");
            }

            return ResponseEntity.ok(editedProduct);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is already a product with the same name");
        }
    }

    @PostMapping("/new")
public ResponseEntity<?> createProduct(@ModelAttribute ProductDTO newProductDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException {
    logger.info("Received request to create a new product with name: {}", newProductDto.getName());
    logger.info("------------------in controller-------------------");
    
    // Validaciones para cantidad y precio
    if (newProductDto.getQuantity() < 0) {
        return ResponseEntity.badRequest().body("La cantidad no puede ser negativa");
    }
    if (newProductDto.getCost() < 0) {
        return ResponseEntity.badRequest().body("El precio del costo no puede ser negativo");
    }
    if (newProductDto.getPrice() < 0) {
        return ResponseEntity.badRequest().body("El precio no puede ser negativo");
    }

    try {
        Product newProduct = productService.createProduct(newProductDto);
        logger.info("Product created successfully with ID: {}", newProduct.getId());

        // Subir imagen del producto
        try {
            minioService.uploadFile("p-" + newProduct.getId().toString(), newProductDto.getPicture());
            logger.info("Image uploaded successfully for product with ID: {}", newProduct.getId());
        } catch (IOException e) {
            logger.error("Error uploading image for product with ID: {}", newProduct.getId(), e);
            productService.deleteProduct(newProduct.getId());
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("Error during file upload for product with ID: {}", newProduct.getId(), e);
            productService.deleteProduct(newProduct.getId()); 
            return ResponseEntity.badRequest().body("Error uploading photo");
        }

        return ResponseEntity.ok(newProduct);
    } catch (DataIntegrityViolationException e) {
        logger.error("Product with name '{}' already exists", newProductDto.getName());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("There is already a product with the same name");
    }
}

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);

            try {
                minioService.deleteFile("p-" + id.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error deleting photo");
            }

            return ResponseEntity.ok("Product deleted");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    @GetMapping("/entrepreneurship/{id_user_entity}")
    public Page<ProductInfoDTO> getProductsByEntrepreneurship(Pageable pageable, @PathVariable("id_user_entity") Long userId) {
        try {
            List<Product> productsPage = productService.getProductsByEntrepreneurshipId(userId);
            List<ProductInfoDTO> productsDTO = new ArrayList<>();
    
            for (Product product : productsPage) {
                ProductInfoDTO productDTO = new ProductInfoDTO(
                    product.getId(),
                    product.getName(),
                    product.getQuantity(),
                    product.getPrice(),
                    product.getCost(),
                    product.getDescription(),
                    IOUtils.toByteArray(minioService.getObject(product.getMultimedia()))
                );
                productsDTO.add(productDTO);
            }
    
            // Construir respuesta con datos de paginación
            Map<String, Object> response = new HashMap<>();
            response.put("products", productsDTO);

            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), productsDTO.size());
            List<ProductInfoDTO> paginatedList = productsDTO.subList(start, end);

            return new PageImpl<>(paginatedList, pageable, productsDTO.size());
            
        } catch (NoSuchElementException e) {
            return (Page<ProductInfoDTO>) ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrepreneurship not found");
            //ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrepreneurship not found");
        } catch (Exception e) {
            logger.error("Error fetching products for userId ID: {}", userId, e);
            return (Page<ProductInfoDTO>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching products");
        }
    }
    

}