// OrderController.java
package com.cropdeal.orderservice.controller;

import com.cropdeal.orderservice.dto.OrderRequestDTO;
import com.cropdeal.orderservice.entity.Order;
import com.cropdeal.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public Order placeOrder(@Valid @RequestBody OrderRequestDTO dto) {
        return orderService.placeOrder(dto);
    }

    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return "Order deleted successfully";
    }

    // New endpoint to get orders by dealer email
    @GetMapping("/dealer/{email}")
    public List<Order> getByDealer(@PathVariable String email) {
        return orderService.getOrdersByDealerEmail(email);
    }
}
