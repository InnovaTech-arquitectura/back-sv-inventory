package com.innovatech.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.innovatech.inventory.controller.OrderController;
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

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private CityService cityService;

    @Mock
    private StateService stateService;

    @Mock
    private OrderStateService orderStateService;

    @Mock
    private ProductService productService;

    @Mock
    private ServiceService serviceService;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private OrderServiceEntityRepository orderServiceEntityRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateOrder() {
        // Arrange
        NewOrderDTO newOrderDTO = new NewOrderDTO();
        newOrderDTO.setSaleNumber("001");
        newOrderDTO.setAdditionalInfo("Some info");
        newOrderDTO.setAddress("123 Street");
        newOrderDTO.setCityId(1L);
        newOrderDTO.setStateId(1L);
        newOrderDTO.setOrderStateId(1L);
        newOrderDTO.setProductIds(List.of(1L, 2L));
        newOrderDTO.setProductQuantities(Map.of(1L, 5, 2L, 10));
        newOrderDTO.setServiceIds(List.of(1L));

        City city = new City();
        city.setId(1L);
        city.setName("Test City");

        State state = new State();
        state.setId(1L);
        state.setName("Test State");

        OrderState orderState = new OrderState();
        orderState.setId(1L);
        orderState.setState("Pending");

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        ServiceS service1 = new ServiceS();
        service1.setId(1L);
        service1.setName("Service 1");

        Order order = Order.builder()
                .id(1L)
                .sale_number("001")
                .additional_info("Some info")
                .address("123 Street")
                .city(city)
                .orderState(orderState)
                .build();

        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setProduct(product1);
        orderProduct1.setQuantity(5);
        orderProduct1.setOrder(order);

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setProduct(product2);
        orderProduct2.setQuantity(10);
        orderProduct2.setOrder(order);

        OrderServiceEntity orderServiceEntity = new OrderServiceEntity();
        orderServiceEntity.setService(service1);
        orderServiceEntity.setOrder(order);

        Mockito.when(cityService.findById(1L)).thenReturn(city);
        Mockito.when(stateService.findById(1L)).thenReturn(state);
        Mockito.when(orderStateService.findById(1L)).thenReturn(orderState);
        Mockito.when(productService.findById(1L)).thenReturn(product1);
        Mockito.when(productService.findById(2L)).thenReturn(product2);
        Mockito.when(serviceService.findById(1L)).thenReturn(service1);

        Mockito.when(orderService.save(Mockito.any(Order.class))).thenReturn(order);

        // Mocks para guardar productos y servicios
        Mockito.when(orderProductRepository.saveAll(Mockito.anyList())).thenReturn(List.of(orderProduct1, orderProduct2));
        Mockito.when(orderServiceEntityRepository.saveAll(Mockito.anyList())).thenReturn(List.of(orderServiceEntity));

        // Act
        ResponseEntity<?> response = orderController.createOrder(newOrderDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Order created", response.getBody());

        // Verificar que los productos y servicios se guarden correctamente
        Mockito.verify(orderProductRepository).saveAll(Mockito.anyList());
        Mockito.verify(orderServiceEntityRepository).saveAll(Mockito.anyList());
    }

    @Test
    public void testGetAllOrders() {
        City city = new City();
        city.setId(1L);
        city.setName("Test City");

        OrderState orderState = new OrderState();
        orderState.setId(1L);
        orderState.setState("Pending");

        Order order1 = Order.builder()
                .id(1L)
                .sale_number("001")
                .additional_info("Some info 1")
                .address("123 Street")
                .city(city)
                .orderState(orderState)
                .build();

        Order order2 = Order.builder()
                .id(2L)
                .sale_number("002")
                .additional_info("Some info 2")
                .address("456 Street")
                .city(city)
                .orderState(orderState)
                .build();

        List<Order> orders = List.of(order1, order2);

        Mockito.when(orderService.findAll(Mockito.any(PageRequest.class))).thenReturn(new PageImpl<>(orders));

        // Act
        ResponseEntity<?> response = orderController.getAllOrders(10, 0);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Map<String, Object>> responseBody = (List<Map<String, Object>>) response.getBody();
        assertEquals(2, responseBody.size());
        assertEquals("001", responseBody.get(0).get("sale_number"));
        assertEquals("002", responseBody.get(1).get("sale_number"));
    }

    @Test
    public void testGetOrderById() {
        City city = new City();
        city.setId(1L);
        city.setName("Test City");

        OrderState orderState = new OrderState();
        orderState.setId(1L);
        orderState.setState("Pending");

        Order order = Order.builder()
                .id(1L)
                .sale_number("001")
                .additional_info("Some info")
                .address("123 Street")
                .city(city)
                .orderState(orderState)
                .build();

        Mockito.when(orderService.findById(1L)).thenReturn(order);

        // Act
        ResponseEntity<?> response = orderController.getOrderById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("001", responseBody.get("sale_number"));
        assertEquals("123 Street", responseBody.get("address"));
    }

    @Test
    public void testGetProductsAndServicesByOrderId() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        ServiceS service1 = new ServiceS();
        service1.setId(1L);
        service1.setName("Service 1");

        Order order = Order.builder()
                .id(1L)
                .sale_number("001")
                .additional_info("Some info")
                .address("123 Street")
                .build();

        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setProduct(product1);
        orderProduct1.setQuantity(5);
        orderProduct1.setOrder(order);

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setProduct(product2);
        orderProduct2.setQuantity(3);
        orderProduct2.setOrder(order);

        OrderServiceEntity orderService1 = new OrderServiceEntity();
        orderService1.setService(service1);
        orderService1.setOrder(order);

        order.setOrderProducts(List.of(orderProduct1, orderProduct2));
        order.setOrderServices(List.of(orderService1));

        Mockito.when(orderService.findById(1L)).thenReturn(order);

        // Act
        ResponseEntity<?> response = orderController.getProductsAndServicesByOrderId(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        List<Map<String, Object>> products = (List<Map<String, Object>>) responseBody.get("products");
        List<Map<String, Object>> services = (List<Map<String, Object>>) responseBody.get("services");

        assertEquals(2, products.size());
        assertEquals(1, services.size());

        assertEquals("Product 1", products.get(0).get("productName"));
        assertEquals(5, products.get(0).get("quantity"));

        assertEquals("Service 1", services.get(0).get("serviceName"));
    }

    @Test
    public void testUpdateOrderState() {
        Order order = new Order();
        order.setId(1L);
        OrderState oldState = new OrderState();
        oldState.setId(1L);
        oldState.setState("Pending");
        order.setOrderState(oldState);

        OrderState newState = new OrderState();
        newState.setId(2L);
        newState.setState("Shipped");

        Mockito.when(orderService.findById(1L)).thenReturn(order);
        Mockito.when(orderStateService.findById(2L)).thenReturn(newState);

        // Act
        ResponseEntity<?> response = orderController.updateOrderState(1L, 2L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order state updated successfully", response.getBody());
        Mockito.verify(orderService).save(order);
        assertEquals(2L, order.getOrderState().getId());
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order();
        order.setId(1L);

        Mockito.when(orderService.findById(1L)).thenReturn(order);

        // Act
        ResponseEntity<?> response = orderController.deleteOrder(1L);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Order deleted successfully", response.getBody());
        Mockito.verify(orderService).deleteById(1L);
    }
}
