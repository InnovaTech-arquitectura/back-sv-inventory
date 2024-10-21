package com.innovatech.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.innovatech.inventory.entity.Order;
import com.innovatech.inventory.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // http://localhost:8090/order/all?limit=n&page=m
    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            return ResponseEntity.ok(orderService.findAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    // http://localhost:8090/order/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        try {
            // Convert the ID to Long and fetch the order
            Long orderId = Long.parseLong(id);
            Order order = orderService.findById(orderId);

            if (order == null) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
    }

    // http://localhost:8090/order/add
    @PostMapping("/add")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            Order createdOrder = orderService.save(order);

            if (createdOrder == null) {
                return new ResponseEntity<>("Unable to create Order", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to create Order", HttpStatus.BAD_REQUEST);
        }
    }

    // http://localhost:8090/order/update
    @PutMapping("/update")
    public ResponseEntity<?> updateOrder(@RequestBody Order order) {
        try {
            // Check if the order exists
            Order existingOrder = orderService.findById(order.getId());

            if (existingOrder == null) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            Order updatedOrder = orderService.save(order);
            return new ResponseEntity<>("Order updated successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to update Order", HttpStatus.BAD_REQUEST);
        }
    }

    // http://localhost:8090/order/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            // Check if the order exists
            Order existingOrder = orderService.findById(id);

            if (existingOrder == null) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            orderService.deleteById(id);
            return new ResponseEntity<>("Order deleted successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to delete Order. Please check the request", HttpStatus.BAD_REQUEST);
        }
    }
}
