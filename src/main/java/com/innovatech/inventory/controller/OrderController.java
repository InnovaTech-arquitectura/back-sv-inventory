package com.innovatech.inventory.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.innovatech.inventory.dto.NewOrderDTO;
import com.innovatech.inventory.entity.City;
import com.innovatech.inventory.entity.Order;
import com.innovatech.inventory.entity.OrderProduct;
import com.innovatech.inventory.entity.OrderServiceEntity;
import com.innovatech.inventory.entity.OrderState;
import com.innovatech.inventory.entity.Product;
import com.innovatech.inventory.entity.ServiceS;
import com.innovatech.inventory.entity.State;
import com.innovatech.inventory.repository.OrderProductRepository;
import com.innovatech.inventory.repository.OrderServiceEntityRepository;
import com.innovatech.inventory.service.CityService;
import com.innovatech.inventory.service.OrderService;
import com.innovatech.inventory.service.OrderStateService;
import com.innovatech.inventory.service.ProductService;
import com.innovatech.inventory.service.ServiceService;
import com.innovatech.inventory.service.StateService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CityService cityService;

    @Autowired
    private StateService stateService;

    @Autowired
    private OrderStateService orderStateService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderServiceEntityRepository orderServiceEntityRepository;

    // http://localhost:8090/order/all?limit=n&page=m
    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            List<Order> orders = orderService.findAll(pageable).getContent();

            // Agregar la información de la ciudad y el estado en la respuesta
            List<Map<String, Object>> response = orders.stream().map(order -> {
                Map<String, Object> orderData = new HashMap<>();
                orderData.put("id", order.getId());
                orderData.put("sale_number", order.getSale_number());
                orderData.put("additional_info", order.getAdditional_info());
                orderData.put("address", order.getAddress());

                // Estado de la orden
                OrderState orderState = order.getOrderState();
                if (orderState != null) {
                    Map<String, Object> orderStateData = new HashMap<>();
                    orderStateData.put("id", orderState.getId());
                    orderStateData.put("state", orderState.getState());
                    orderData.put("orderState", orderStateData);
                }

                // Ciudad y estado
                City city = order.getCity();
                if (city != null) {
                    Map<String, Object> cityData = new HashMap<>();
                    cityData.put("id", city.getId());
                    cityData.put("name", city.getName());

                    State state = city.getState();
                    if (state != null) {
                        Map<String, Object> stateData = new HashMap<>();
                        stateData.put("id", state.getId());
                        stateData.put("name", state.getName());
                        cityData.put("state", stateData);
                    }

                    orderData.put("city", cityData);
                }

                return orderData;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }


    // http://localhost:8090/order/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        try {
            // Convertir el ID a Long y buscar la orden
            Long orderId = Long.parseLong(id);
            Order order = orderService.findById(orderId);

            if (order == null) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            // Construir la respuesta para incluir la información de la orden, ciudad y estado
            Map<String, Object> response = new HashMap<>();
            response.put("id", order.getId());
            response.put("sale_number", order.getSale_number());
            response.put("additional_info", order.getAdditional_info());
            response.put("address", order.getAddress());

            // Estado de la orden
            OrderState orderState = order.getOrderState();
            if (orderState != null) {
                Map<String, Object> orderStateData = new HashMap<>();
                orderStateData.put("id", orderState.getId());
                orderStateData.put("state", orderState.getState());
                response.put("orderState", orderStateData);
            }

            // Información de la ciudad y estado
            City city = order.getCity();
            if (city != null) {
                Map<String, Object> cityData = new HashMap<>();
                cityData.put("id", city.getId());
                cityData.put("name", city.getName());

                State state = city.getState();
                if (state != null) {
                    Map<String, Object> stateData = new HashMap<>();
                    stateData.put("id", state.getId());
                    stateData.put("name", state.getName());
                    cityData.put("state", stateData);
                }

                response.put("city", cityData);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
    }

    // http://localhost:8090/order/{id}/details
    @GetMapping("/{id}/details")
    public ResponseEntity<?> getProductsAndServicesByOrderId(@PathVariable Long id) {
        try {
            // Verificar si la orden existe
            Order order = orderService.findById(id);
            if (order == null) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            // Obtener los productos asociados a la orden
            List<OrderProduct> orderProducts = order.getOrderProducts();
            List<Map<String, Object>> productsData = orderProducts.stream().map(orderProduct -> {
                Map<String, Object> productData = new HashMap<>();
                productData.put("productId", orderProduct.getProduct().getId());
                productData.put("productName", orderProduct.getProduct().getName());
                productData.put("quantity", orderProduct.getQuantity());
                return productData;
            }).collect(Collectors.toList());

            // Obtener los servicios asociados a la orden
            List<OrderServiceEntity> orderServices = order.getOrderServices();
            List<Map<String, Object>> servicesData = orderServices.stream().map(orderService -> {
                Map<String, Object> serviceData = new HashMap<>();
                serviceData.put("serviceId", orderService.getService().getId());
                serviceData.put("serviceName", orderService.getService().getName());
                return serviceData;
            }).collect(Collectors.toList());

            // Construir la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.getId());
            response.put("products", productsData);
            response.put("services", servicesData);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to retrieve order details", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // http://localhost:8090/order/add
    @PostMapping("/add")
    public ResponseEntity<?> createOrder(@RequestBody NewOrderDTO newOrderDTO) {
        try {
            // Buscar la ciudad por ID
            City city = cityService.findById(newOrderDTO.getCityId());
            if (city == null) {
                return new ResponseEntity<>("City not found", HttpStatus.NOT_FOUND);
            }

            // Buscar el estado por ID
            State state = stateService.findById(newOrderDTO.getStateId());
            if (state == null) {
                return new ResponseEntity<>("State not found", HttpStatus.NOT_FOUND);
            }

            // Buscar el estado del pedido por ID
            OrderState orderState = orderStateService.findById(newOrderDTO.getOrderStateId());
            if (orderState == null) {
                return new ResponseEntity<>("Order State not found", HttpStatus.NOT_FOUND);
            }

            // Validar que todos los productos existen
            if (newOrderDTO.getProductIds() != null && !newOrderDTO.getProductIds().isEmpty()) {
                for (Long productId : newOrderDTO.getProductIds()) {
                    Product product = productService.findById(productId);
                    if (product == null) {
                        return new ResponseEntity<>("Product with ID " + productId + " not found", HttpStatus.NOT_FOUND);
                    }
                }
            }

            // Validar que todos los servicios existen
            if (newOrderDTO.getServiceIds() != null && !newOrderDTO.getServiceIds().isEmpty()) {
                for (Long serviceId : newOrderDTO.getServiceIds()) {
                    ServiceS service = serviceService.findById(serviceId);
                    if (service == null) {
                        return new ResponseEntity<>("Service with ID " + serviceId + " not found", HttpStatus.NOT_FOUND);
                    }
                }
            }

            // Crear una nueva orden
            Order order = new Order();
            order.setSale_number(newOrderDTO.getSaleNumber());
            order.setAdditional_info(newOrderDTO.getAdditionalInfo());
            order.setAddress(newOrderDTO.getAddress());
            order.setCity(city);
            order.setOrderState(orderState);

            // Guardar la orden en la base de datos antes de asignar productos/servicios
            Order createdOrder = orderService.save(order);

            // Asignar productos a la orden (si los hay)
            if (newOrderDTO.getProductIds() != null && !newOrderDTO.getProductIds().isEmpty()) {
                List<OrderProduct> orderProducts = newOrderDTO.getProductIds().stream()
                    .map(productId -> {
                        Product product = productService.findById(productId);
                        OrderProduct orderProduct = new OrderProduct();
                        orderProduct.setProduct(product);
                        orderProduct.setQuantity(newOrderDTO.getProductQuantities().get(productId));
                        orderProduct.setOrder(createdOrder);
                        return orderProduct;
                    })
                    .collect(Collectors.toList());

                // Guardar los productos en la tabla Order_Product
                if (!orderProducts.isEmpty()) {
                    orderProductRepository.saveAll(orderProducts);
                }
            }

            // Asignar servicios a la orden (si los hay)
            if (newOrderDTO.getServiceIds() != null && !newOrderDTO.getServiceIds().isEmpty()) {
                List<OrderServiceEntity> orderServices = newOrderDTO.getServiceIds().stream()
                    .map(serviceId -> {
                        ServiceS service = serviceService.findById(serviceId);
                        OrderServiceEntity orderService = new OrderServiceEntity();
                        orderService.setService(service);
                        orderService.setOrder(createdOrder);
                        return orderService;
                    })
                    .collect(Collectors.toList());

                // Guardar los servicios en la tabla Order_Service
                if (!orderServices.isEmpty()) {
                    orderServiceEntityRepository.saveAll(orderServices);
                }
            }

            return new ResponseEntity<>("Order created", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to create Order", HttpStatus.BAD_REQUEST);
        }
    }



    // http://localhost:8090/order/update
    @PutMapping("/update")
    public ResponseEntity<?> updateOrderState(@RequestParam Long orderId, @RequestParam Long newOrderStateId) {
        try {
            // Verificar si la orden existe
            Order existingOrder = orderService.findById(orderId);
            if (existingOrder == null) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            // Verificar si el nuevo estado de la orden existe
            OrderState newOrderState = orderStateService.findById(newOrderStateId);
            if (newOrderState == null) {
                return new ResponseEntity<>("Order State not found", HttpStatus.NOT_FOUND);
            }

            // Actualizar solo el estado de la orden
            existingOrder.setOrderState(newOrderState);

            // Guardar los cambios en la orden
            orderService.save(existingOrder);

            return new ResponseEntity<>("Order state updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to update Order state", HttpStatus.BAD_REQUEST);
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
