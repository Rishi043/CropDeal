//
//package com.cropdeal.paymentservice.service;
//
//import com.cropdeal.paymentservice.client.CropClient;
//import com.cropdeal.paymentservice.client.NotificationClient;
//import com.cropdeal.paymentservice.client.OrderClient;
//import com.cropdeal.paymentservice.dto.*;
//import com.cropdeal.paymentservice.repository.PaymentRepository;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.checkout.Session;
//import com.stripe.param.checkout.SessionCreateParams;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.github.resilience4j.retry.annotation.Retry;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class StripeService {
//
//    @Value("${stripe.api.key}")
//    private String secretKey;
//
//    private final PaymentRepository paymentRepository;
//    private final CropClient cropClient;
//    private final OrderClient orderClient;
//    private final NotificationClient notificationClient;
//
//    public StripeService(PaymentRepository paymentRepository, CropClient cropClient,
//                         OrderClient orderClient, NotificationClient notificationClient) {
//        this.paymentRepository = paymentRepository;
//        this.cropClient = cropClient;
//        this.orderClient = orderClient;
//        this.notificationClient = notificationClient;
//    }
//
//    // ✅ Automatically set Stripe API key on app startup
//    @PostConstruct
//    public void init() {
//        Stripe.apiKey = secretKey;
//    }
//
//    @CircuitBreaker(name = "cropClientBreaker", fallbackMethod = "fallbackCrop")
//    @Retry(name = "cropClientBreaker", fallbackMethod = "fallbackCrop")
//    public Crop getCropByName(String cropName) {
//        List<Crop> crops = cropClient.getCropByName(cropName).getBody();
//        return (crops != null && !crops.isEmpty()) ? crops.get(0) : null;
//    }
//
//    public Crop fallbackCrop(String cropName, Exception e) {
//        System.out.println("⚠️ Fallback triggered for getCropByName: " + e.getMessage());
//        return null;
//    }
//
//    public StripeResponse checkoutCrops(PurchaseRequest purchaseRequest) {
//        Crop crop = getCropByName(purchaseRequest.getCrop());
//
//        if (crop == null) {
//            System.out.println("❌ Crop is null — returning fallback response.");
//            return StripeResponse.builder()
//                    .status("FAILURE")
//                    .message("Crop not found or crop-service is unavailable.")
//                    .build();
//        }
//
//        Long cropId = crop.getId();
//        long priceInCents = (long) (crop.getPricePerKg() * 100); // Convert to smallest currency unit
//
//        SessionCreateParams.LineItem.PriceData.ProductData productData =
//                SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                        .setName(purchaseRequest.getCrop())
//                        .build();
//
//        SessionCreateParams.LineItem.PriceData cropPrice =
//                SessionCreateParams.LineItem.PriceData.builder()
//                        .setCurrency(purchaseRequest.getCurrency() == null ? "usd" : purchaseRequest.getCurrency().toLowerCase())
//                        .setUnitAmount(priceInCents)
//                        .setProductData(productData)
//                        .build();
//
//        SessionCreateParams.LineItem lineItem =
//                SessionCreateParams.LineItem.builder()
//                        .setQuantity((long) purchaseRequest.getQuantity())
//                        .setPriceData(cropPrice)
//                        .build();
//
//        SessionCreateParams params =
//                SessionCreateParams.builder()
//                        .setMode(SessionCreateParams.Mode.PAYMENT)
//                        .setSuccessUrl("http://localhost:8085/payment/success/id/" + cropId +
//                                "/order/" + purchaseRequest.getOrderId() +
//                                "?quantity=" + purchaseRequest.getQuantity())
//                        .setCancelUrl("http://localhost:8085/payment/cancel")
//                        .addLineItem(lineItem)
//                        .build();
//
//        try {
//            Session session = Session.create(params);
//            paymentRepository.save(purchaseRequest);
//
//            return StripeResponse.builder()
//                    .status("SUCCESS")
//                    .message("Payment session created successfully")
//                    .sessionId(session.getId())
//                    .sessionUrl(session.getUrl())
//                    .build();
//
//        } catch (StripeException e) {
//            return StripeResponse.builder()
//                    .status("FAILURE")
//                    .message("Stripe session creation failed: " + e.getMessage())
//                    .build();
//        }
//    }
//
//    public String deleteCropById(Long id) {
//        return cropClient.deleteCrop(id).getBody();
//    }
//
//    public String handlePaymentSuccess(Long cropId, Long orderId, int quantity) {
//        cropClient.reduceAndDelete(cropId, quantity);
//        orderClient.updateOrderStatus(orderId, "SUCCESS");
//
//        Crop crop = cropClient.getCropById(cropId).getBody();
//        Order order = orderClient.getOrderById(orderId).getBody();
//        PurchaseRequest payment = paymentRepository.findByOrderId(orderId);
//
//        if (crop == null || order == null || payment == null) {
//            return "❌ Payment processing failed due to missing data.";
//        }
//
//        double amount = crop.getPricePerKg() * quantity;
//        String transactionId = payment.getId().toString();
//
//        EmailRequest emailRequest = new EmailRequest();
//        emailRequest.setTo(order.getDealerEmail());
//        emailRequest.setCropName(crop.getName());
//        emailRequest.setAmount(amount);
//        emailRequest.setTransactionId(transactionId);
//        emailRequest.setPaymentDate(LocalDate.now().toString());
//        emailRequest.setQuantity(quantity);
//        emailRequest.setCurrency(payment.getCurrency());
//
//        try {
//            notificationClient.sendPaymentEmail(emailRequest);
//        } catch (Exception e) {
//            System.out.println("❌ Failed to send payment email: " + e.getMessage());
//        }
//
//        return "✅ Payment processed successfully. Order status updated and confirmation email sent.";
//    }
//}

package com.cropdeal.paymentservice.service;

import com.cropdeal.paymentservice.client.CropClient;
import com.cropdeal.paymentservice.client.NotificationClient;
import com.cropdeal.paymentservice.client.OrderClient;
import com.cropdeal.paymentservice.dto.*;
import com.cropdeal.paymentservice.entity.Payment;
import com.cropdeal.paymentservice.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String secretKey;

    private final PaymentRepository paymentRepository;
    private final CropClient cropClient;
    private final OrderClient orderClient;
    private final NotificationClient notificationClient;

    public StripeService(PaymentRepository paymentRepository, CropClient cropClient,
                         OrderClient orderClient, NotificationClient notificationClient) {
        this.paymentRepository = paymentRepository;
        this.cropClient = cropClient;
        this.orderClient = orderClient;
        this.notificationClient = notificationClient;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public StripeResponse checkoutCrops(PurchaseRequest purchaseRequest) {
        Long orderId = purchaseRequest.getOrderId();

        // Step 1: Get order
        Order order = orderClient.getOrderById(orderId).getBody();
        if (order == null) {
            return StripeResponse.builder()
                    .status("FAILURE")
                    .message("❌ Order not found for ID: " + orderId)
                    .build();
        }

        // Step 2: Get crop
        Crop crop = cropClient.getCropById(order.getCropId()).getBody();
        if (crop == null) {
            return StripeResponse.builder()
                    .status("FAILURE")
                    .message("❌ Crop not found for ID: " + order.getCropId())
                    .build();
        }

        // Step 3: Prepare Stripe session
        long unitAmountInCents = (long) (crop.getPricePerKg() * 100);

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(crop.getName())
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmount(unitAmountInCents)
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity((long) order.getQuantity())
                        .setPriceData(priceData)
                        .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8085/payment/success/id/" + crop.getId()
                        + "/order/" + order.getOrderId()
                        + "?quantity=" + order.getQuantity())
                .setCancelUrl("http://localhost:8085/payment/cancel")
                .addLineItem(lineItem)
                .build();

        try {
            Session session = Session.create(params);

            // Step 4: Save payment with status PENDING
            Payment payment = Payment.builder()
                    .orderId(orderId)
                    .dealerEmail(order.getDealerEmail())
                    .cropName(crop.getName())
                    .quantity(order.getQuantity())
                    .amount(crop.getPricePerKg() * order.getQuantity())
                    .paymentDate(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);

            // Step 5: Return Stripe session info
            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("✅ Stripe session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();

        } catch (StripeException e) {
            return StripeResponse.builder()
                    .status("FAILURE")
                    .message("❌ Stripe error: " + e.getMessage())
                    .build();
        }
    }

    public String handlePaymentSuccess(Long cropId, Long orderId, int quantity) {
        cropClient.reduceAndDelete(cropId, quantity);
        orderClient.updateOrderStatus(orderId, "SUCCESS");

        Crop crop = cropClient.getCropById(cropId).getBody();
        Order order = orderClient.getOrderById(orderId).getBody();
        Payment payment = paymentRepository.findByOrderId(orderId);

        if (crop == null || order == null || payment == null) {
            return "❌ Payment processing failed due to missing data.";
        }

        double amount = crop.getPricePerKg() * quantity;

        // ✅ Prepare email
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(order.getDealerEmail());
        emailRequest.setCropName(crop.getName());
        emailRequest.setAmount(amount);
        emailRequest.setTransactionId(payment.getPaymentId().toString());
        emailRequest.setPaymentDate(payment.getPaymentDate().toString());
        emailRequest.setQuantity(quantity);
        emailRequest.setCurrency("USD");

        try {
            notificationClient.sendPaymentEmail(emailRequest);
        } catch (Exception e) {
            System.out.println("❌ Failed to send payment email: " + e.getMessage());
        }

        return "✅ Payment processed successfully. Order status updated and confirmation email sent.";
    }
}
