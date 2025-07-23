package com.cropdeal.paymentservice.controller;

import com.cropdeal.paymentservice.dto.Crop;
import com.cropdeal.paymentservice.dto.PurchaseRequest;
import com.cropdeal.paymentservice.dto.StripeResponse;
import com.cropdeal.paymentservice.service.StripeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

//    @PostMapping("/checkout")
//    public ResponseEntity<StripeResponse> checkoutProduct(@RequestBody PurchaseRequest purchaseRequest) {
//        StripeResponse stripeResponse = stripeService.checkoutCrops(purchaseRequest);
//        return ResponseEntity.status(HttpStatus.OK).body(stripeResponse);
//    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkout(@RequestBody PurchaseRequest request) {
        StripeResponse response = stripeService.checkoutCrops(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/success/id/{cropId}/order/{orderId}")
    public ResponseEntity<String> success(@PathVariable Long cropId, @PathVariable Long orderId,@RequestParam  Integer quantity) {
        String result = stripeService.handlePaymentSuccess(cropId, orderId, quantity);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancel() {
        return ResponseEntity.ok("Payment failed or cancelled.");
    }


//    // ✅ New endpoint to test Circuit Breaker on getCropByName
//    @GetMapping("/crops/by-name/{name}")
//    public ResponseEntity<?> getCropByName(@PathVariable String name) {
//    Crop crop = stripeService.getCropByName(name);
//    if (crop == null) {
//    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//            .body("Crop service unavailable (fallback triggered).");
//    }
//    return ResponseEntity.ok(crop);
//    }
}


