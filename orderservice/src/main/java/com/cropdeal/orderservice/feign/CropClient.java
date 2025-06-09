package com.cropdeal.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "crop-service")
public interface CropClient {
    @GetMapping("/crops/{id}")
    Object getCropById(@PathVariable Long id); // Can later replace with DTO
}
