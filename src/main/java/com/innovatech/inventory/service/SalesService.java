package com.innovatech.inventory.service;


import com.innovatech.inventory.entity.Product;
import com.innovatech.inventory.entity.Sales;
import com.innovatech.inventory.repository.ProductRepository;
import com.innovatech.inventory.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class SalesService implements CrudService<Sales, Long>{
    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Sales findById(Long id)  {
        return salesRepository.findById(id).orElse(null);
    }

    public Page<Sales> findAll(Pageable pageable) {
        // Obtén todas las ventas paginadas
        Page<Sales> salesPage = salesRepository.findAll(pageable);
    
        // Itera sobre cada venta para actualizar el stock
        for (Sales sale : salesPage.getContent()) {
            Product product = sale.getProduct();
            if (product != null) {
                // Resta la cantidad vendida al stock del producto
                product.setQuantity(product.getQuantity() - sale.getQuantitySold());
                productRepository.save(product); // Guarda el producto actualizado
            }
        }
    
        return salesPage; 
    }
       
    @Override
    public void deleteById(Long id) {
        // Your implementation here
        salesRepository.deleteById(id);
    }

    public Sales save(Sales sale) {
        // Verificar que el producto existe
        Product product = productRepository.findById(sale.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + sale.getProduct().getId()));

        // Verificar que haya suficiente stock
        if (product.getQuantity() == null || sale.getQuantitySold() > product.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getId());
        }

        // Actualizar el stock del producto
        product.setQuantity(product.getQuantity() - sale.getQuantitySold());
        productRepository.save(product); // Guarda el producto actualizado

        // Guardar la venta
        return salesRepository.save(sale);
    }

    public Page<Sales> findSalesByEntrepreneurshipId(Long entrepreneurshipId, Pageable pageable) {
        return salesRepository.findByEntrepreneurshipId(entrepreneurshipId, pageable);
    }

}               