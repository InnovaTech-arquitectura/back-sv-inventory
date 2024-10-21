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

    public List<Sales> listSales() {
        return salesRepository.findAll(); // Devuelve todas las ventas
    }
    
    @Override
    public void deleteById(Long id) {
        // Your implementation here
        salesRepository.deleteById(id);
    }

    @Override
    public Sales save(Sales sale) {
        // Lógica adicional si es necesario (por ejemplo, actualizar el stock)
        Product product = sale.getProduct();
        product.setQuantity(product.getQuantity() - sale.getQuantitySold());
        productRepository.save(product); // Actualizar el stock del producto

        return salesRepository.save(sale); // Guardar la venta
    }

    public Page<Sales> findAll(Pageable pageable) {
        return salesRepository.findAll(pageable);
    }
}               