package com.cropdeal.paymentservice.repository;

import com.cropdeal.paymentservice.dto.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PurchaseRequest, Long> {
}

