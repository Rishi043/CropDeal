
package com.cropdeal.orderservice.service;

import com.cropdeal.orderservice.dto.OrderRequestDTO;
import com.cropdeal.orderservice.entity.Order;
import com.cropdeal.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepo;

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
    void testGetAllOrders() {
        when(orderRepo.findAll()).thenReturn(List.of(order));
        List<Order> result = orderService.getAllOrders();
        assertEquals(1, result.size());
    }

    @Test
    void testPlaceOrder() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setCropId(101L);
        dto.setDealerEmail("dealer@example.com");

        when(orderRepo.saveAndFlush(any(Order.class))).thenReturn(order);
        Order result = orderService.placeOrder(dto);
        assertEquals("dealer@example.com", result.getDealerEmail());
    }

    @Test
    void testUpdateOrderStatusByCropId() {
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        orderService.updateOrderStatusByCropId(1L, "COMPLETED");
        verify(orderRepo).save(order);
        assertEquals("COMPLETED", order.getStatus());
    }

    @Test
    void testDeleteOrder() {
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        orderService.deleteOrder(1L);
        verify(orderRepo).deleteById(1L);
    }

    @Test
    void testGetOrdersByDealerEmail() {
        when(orderRepo.findByDealerEmail("dealer@example.com")).thenReturn(List.of(order));
        List<Order> result = orderService.getOrdersByDealerEmail("dealer@example.com");
        assertEquals(1, result.size());
    }

    @Test
    void testGetOrderById() {
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        Order result = orderService.getOrderById(1L);
        assertEquals("dealer@example.com", result.getDealerEmail());
    }
}
