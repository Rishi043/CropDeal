package com.cropdeal.orderservice.controller;

import com.cropdeal.orderservice.dto.OrderRequestDTO;
import com.cropdeal.orderservice.entity.Order;
import com.cropdeal.orderservice.feign.CropClient;
import com.cropdeal.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private CropClient cropClient;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setOrderId(1L);
        order.setCropId(101L);
        order.setDealerEmail("dealer@example.com");
        order.setOrderDate(LocalDate.now());
        order.setStatus("PENDING");
    }

    @Test
    void testGetAllOrdersOfDealer() {
        when(orderService.getAllOrders()).thenReturn(List.of(order));
        ResponseEntity<List<Order>> response = orderController.getAllOrdersOfDealer();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetOrderById() {
        when(orderService.getOrderById(1L)).thenReturn(order);
        ResponseEntity<Order> response = orderController.getOrderById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("dealer@example.com", response.getBody().getDealerEmail());
    }

    @Test
    void testPlaceOrder() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setCropId(101L);
        dto.setDealerEmail("dealer@example.com");

        when(orderService.placeOrder(any(OrderRequestDTO.class))).thenReturn(order);
        ResponseEntity<String> response = orderController.placeOrder(dto);
        assertTrue(response.getBody().contains("Order placed successfully"));
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderService).deleteOrder(1L);
        String result = orderController.deleteOrder(1L);
        assertEquals("Order deleted successfully", result);
    }

    @Test
    void testUpdateOrderStatus() {
        doNothing().when(orderService).updateOrderStatusByCropId(1L, "COMPLETED");
        String result = orderController.updateOrderStatus(1L, "COMPLETED");
        assertEquals("Order status updated to COMPLETED", result);
    }

    @Test
    void testGetByDealer() {
        when(orderService.getOrdersByDealerEmail("dealer@example.com")).thenReturn(List.of(order));
        List<Order> result = orderController.getByDealer("dealer@example.com");
        assertEquals(1, result.size());
    }
}

