package com.cropdeal.orderservice.service;

import com.cropdeal.orderservice.dto.OrderRequestDTO;
import com.cropdeal.orderservice.dto.OrderResponseDTO;
import com.cropdeal.orderservice.entity.Order;
import com.cropdeal.orderservice.feign.Crop;
import com.cropdeal.orderservice.feign.CropClient;
import com.cropdeal.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private CropClient cropClient;

    // ✅ Save order using dealerEmail from JWT
    public Order placeOrder(OrderRequestDTO dto, String dealerEmail) {

        // 🔹 Step 1: Fetch crop from crop-service
        ResponseEntity<Crop> cropResponse;
        try {
            cropResponse = cropClient.getCropById(dto.getCropId());
        } catch (Exception e) {
            throw new RuntimeException("Unable to connect to crop-service: " + e.getMessage(), e);
        }

        if (cropResponse == null || !cropResponse.getStatusCode().is2xxSuccessful() || cropResponse.getBody() == null) {
            throw new RuntimeException("Crop not found with ID: " + dto.getCropId());
        }

        Crop crop = cropResponse.getBody();

        // 🔹 Step 2: Validate quantity
        if (dto.getQuantity() > crop.getTotalQuantity()) {
            throw new RuntimeException("Not enough quantity available. Available: " + crop.getTotalQuantity());
        }

        if (dto.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        // 🔹 Step 3: Save order
        Order order = new Order();
        order.setCropId(dto.getCropId());
        order.setQuantity(dto.getQuantity());
        order.setDealerEmail(dealerEmail);
        order.setOrderDate(LocalDate.now());
        order.setStatus("PENDING");

        return orderRepo.saveAndFlush(order);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public void updateOrderStatusByCropId(Long orderId, String status) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        order.setStatus(status);
        orderRepo.save(order);
    }

    public void deleteOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        orderRepo.delete(order);
    }

    public Order getOrderById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
    }

    // ✅ Return orders with crop name
    public List<OrderResponseDTO> getOrdersByDealerEmailWithCropName(String email) {
        return orderRepo.findByDealerEmail(email).stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setOrderId(order.getOrderId());
            dto.setDealerEmail(order.getDealerEmail());
            dto.setQuantity(order.getQuantity());
            dto.setOrderDate(order.getOrderDate());
            dto.setStatus(order.getStatus());

            try {
                String cropName = cropClient.getCropById(order.getCropId()).getBody().getName(); // ✅
                dto.setCropName(cropName);
            } catch (Exception e) {
                dto.setCropName("Unknown");
            }

            return dto;
        }).toList();
    }

    public List<Order> getOrdersByDealerEmail(String email) {
        return orderRepo.findByDealerEmail(email);
    }
}
