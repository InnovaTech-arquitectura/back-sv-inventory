package com.innovatech.inventory.service;

import com.innovatech.inventory.dto.NewSupplierDTO;
import com.innovatech.inventory.entity.Supplier;
import com.innovatech.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.innovatech.inventory.repository.SupplierRepository;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id).orElse(null);
    }

    public List<Supplier> listSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier createSupplier(NewSupplierDTO newSupplier) {
        Supplier supplier = new Supplier();
        supplier.setName(newSupplier.getName());
        supplier.setDescription(newSupplier.getDescription());
        supplier.setContact_number(newSupplier.getContact_number());
        for (Long productId : newSupplier.getProducts()) {
            productRepository.findById(productId).ifPresent(product -> supplier.getProducts().add(product));
        }
        return supplierRepository.save(supplier);
    }

    public Supplier editSupplier(Long id, NewSupplierDTO editedSupplier) {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        if (supplier == null) {
            return null;
        }
        supplier.setName(editedSupplier.getName());
        supplier.setDescription(editedSupplier.getDescription());
        supplier.setContact_number(editedSupplier.getContact_number());
        supplier.getProducts().clear();
        for (Long productId : editedSupplier.getProducts()) {
            productRepository.findById(productId).ifPresent(product -> supplier.getProducts().add(product));
        }
        return supplierRepository.save(supplier);
    }

    public List<Supplier> getSuppliersByProductId(Long productId) {
        return supplierRepository.findByProductsId(productId);
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}
