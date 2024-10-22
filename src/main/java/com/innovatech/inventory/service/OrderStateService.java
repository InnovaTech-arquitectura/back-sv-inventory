package com.innovatech.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

import com.innovatech.inventory.entity.OrderState;
import com.innovatech.inventory.repository.OrderStateRepository;

@Service
public class OrderStateService implements CrudService<OrderState, Long> {

    @Autowired
    private OrderStateRepository orderStateRepository;

    @Override
    public OrderState findById(Long id) {
        return orderStateRepository.findById(id).orElse(null);
    }

    public Page<OrderState> findAll(Pageable pageable) {
        return orderStateRepository.findAll(pageable);
    }

    public List<OrderState> findAll() {
        return orderStateRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        orderStateRepository.deleteById(id);
    }

    @Override
    public OrderState save(OrderState orderState) {
        return orderStateRepository.save(orderState);
    }
}
