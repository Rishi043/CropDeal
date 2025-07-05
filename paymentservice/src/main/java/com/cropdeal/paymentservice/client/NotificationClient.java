package com.cropdeal.paymentservice.client;

import com.cropdeal.paymentservice.dto.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", contextId = "NotificationClient",path = "/notify")

public interface NotificationClient {

    @PostMapping("/payment-success")
    void sendPaymentEmail(@RequestBody EmailRequest request);

}
