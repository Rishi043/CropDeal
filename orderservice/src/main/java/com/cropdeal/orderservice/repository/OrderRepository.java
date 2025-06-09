package com.cropdeal.orderservice.repository;

import com.cropdeal.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByDealerEmail(String dealerEmail);
}
