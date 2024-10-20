package com.innovatech.inventory.service;

import java.io.IOException; 
import java.security.InvalidKeyException; 
import java.security.NoSuchAlgorithmException; 
import java.util.List; 

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.data.domain.PageRequest; 
import org.springframework.stereotype.Service;

import com.innovatech.inventory.dto.ProductDTO;
import com.innovatech.inventory.entity.Product; 
import com.innovatech.inventory.repository.ProductRepository;

import io.minio.errors.ErrorResponseException; 
import io.minio.errors.InsufficientDataException; 
import io.minio.errors.InternalException; 
import io.minio.errors.InvalidResponseException; 
import io.minio.errors.ServerException; 
import io.minio.errors.XmlParserException;


import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MinioService minioService;

    public Product findProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> listProducts(Integer page, Integer limit) {
        PageRequest pageable = PageRequest.of(page - 1, limit);
        return productRepository.findAll(pageable).getContent();
    }

    public Product createProduct(ProductDTO newProductDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException {

        
        Product product = new Product(newProductDto.getName(), newProductDto.getQuantity(), newProductDto.getPrice(), newProductDto.getCost(), newProductDto.getDescription());
        product.setMultimedia("p-" + product.getId().toString());
        Product createdProduct = productRepository.save(product);

        
        // Upload product image to MinIO
        uploadProductImage(createdProduct.getId(), newProductDto.getPicture());

        return createdProduct;
    }

    public Product editProduct(Long id, ProductDTO editedProductDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(editedProductDto.getName());
        product.setQuantity(editedProductDto.getQuantity());
        product.setPrice(editedProductDto.getPrice());
        product.setCost(editedProductDto.getCost());
        product.setDescription(editedProductDto.getDescription());
        product.setMultimedia("p-" + id.toString());

        Product updatedProduct = productRepository.save(product);

        // Upload product image to MinIO
        uploadProductImage(updatedProduct.getId(), editedProductDto.getPicture());

        return updatedProduct;
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Delete the image from MinIO before deleting the product
        try {
            minioService.deleteFile("p-" + id);
        } catch (IOException | ServerException | InsufficientDataException | ErrorResponseException | NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException | InternalException e) {
            throw new RuntimeException("Error deleting image", e);
        }

        productRepository.delete(product);
    }

    // Helper method to upload the product image to MinIO
    private void uploadProductImage(Long productId, MultipartFile picture) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String fileName = "p-" + productId;  // Use the product ID as part of the file name
        minioService.uploadFile(fileName, picture);
    }
}

