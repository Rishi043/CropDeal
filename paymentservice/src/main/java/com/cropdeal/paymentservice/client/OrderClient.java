package com.cropdeal.paymentservice.client;

import com.cropdeal.paymentservice.dto.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", contextId = "OrderClient", path = "/orders")
public interface OrderClient {

    // Update order status by order ID
    @PutMapping("/updateStatus/{orderId}")
    void updateOrderStatus(@PathVariable("orderId") Long orderId,
                           @RequestParam("status") String status);

    @GetMapping("/OrderId/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id);



}

