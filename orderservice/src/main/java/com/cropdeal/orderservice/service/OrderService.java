package com.cropdeal.orderservice.service;

import com.cropdeal.orderservice.dto.OrderRequestDTO;
import com.cropdeal.orderservice.entity.Order;
import com.cropdeal.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order placeOrder(OrderRequestDTO dto) {
        Order order = new Order();
        order.setCropId(dto.getCropId());
        order.setDealerEmail(dto.getDealerEmail());

        order.setOrderDate(LocalDate.now());

        order.setStatus("PENDING");
        return orderRepo.saveAndFlush(order);
    }

    public void updateOrderStatusByCropId(Long orderId, String status) {
        Order order = orderRepo.findById(orderId).orElseThrow(()->new RuntimeException("Order not found by "+orderId));
        if (order != null) {
            order.setStatus(status);
            orderRepo.save(order);
        } else {
            throw new RuntimeException("Order not found for crop ID: " + orderId);
        }
    }

    public void deleteOrder(Long orderId) {
        Optional<Order> order = orderRepo.findById(orderId);
        if (order.isPresent()) {
            orderRepo.deleteById(orderId);
        } else {
            throw new RuntimeException("Order with ID " + orderId + " not found");
        }
    }

    // fetch orders by dealer email
    public List<Order> getOrdersByDealerEmail(String email) {
        return orderRepo.findByDealerEmail(email);
    }

    public Order getOrderById(Long id) {

        return orderRepo.findById(id).orElseThrow(()->new  RuntimeException("Order not found by "+id));
    }
}

