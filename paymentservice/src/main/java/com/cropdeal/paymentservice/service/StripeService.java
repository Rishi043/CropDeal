package com.cropdeal.paymentservice.service;

import com.cropdeal.paymentservice.client.CropClient;
import com.cropdeal.paymentservice.client.OrderClient;
import com.cropdeal.paymentservice.dto.*;
import com.cropdeal.paymentservice.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String secretKey;

    private final PaymentRepository paymentRepository;
    private final CropClient cropClient;
    private final OrderClient orderClient;

    public StripeService(PaymentRepository paymentRepository, CropClient cropClient, OrderClient orderClient) {
        this.paymentRepository = paymentRepository;
        this.cropClient = cropClient;
        this.orderClient = orderClient;
    }

    public StripeResponse checkoutCrops(PurchaseRequest purchaseRequest) {
        Stripe.apiKey = secretKey;

        Crop crop = cropClient.getCropByName(purchaseRequest.getCrop()).getBody();

        if (crop == null) {
            return StripeResponse.builder()
                    .status("FAILURE")
                    .message("Crop not found!")
                    .build();
        }

        Long cropId = crop.getId();

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(purchaseRequest.getCrop())
                        .build();

        SessionCreateParams.LineItem.PriceData cropPrice =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(purchaseRequest.getCurrency() == null ? "USD" : purchaseRequest.getCurrency())
                        .setUnitAmount(purchaseRequest.getAmount())
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()

                        //  .setQuantity() from Stripe's SDK expects a Long
                        .setQuantity((long) purchaseRequest.getQuantity())
                        .setPriceData(cropPrice)
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8085/payment/success/id/" + cropId + "/order/" + purchaseRequest.getOrderId() + "?quantity=" + purchaseRequest.getQuantity())

                        .setCancelUrl("http://localhost:8085/payment/cancel")
                        .addLineItem(lineItem)
                        .build();

        try {
            Session session = Session.create(params);
            paymentRepository.save(purchaseRequest);

            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Payment session created successfully")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();

        } catch (StripeException e) {
            return StripeResponse.builder()
                    .status("FAILURE")
                    .message("Stripe session creation failed: " + e.getMessage())
                    .build();
        }
    }

    public String deleteCropById(Long id) {
        return cropClient.deleteCrop(id).getBody();
    }

    public String handlePaymentSuccess(Long cropId, Long orderId, int quantity) {

        cropClient.reduceAndDelete(cropId, quantity);                      // from crop client
        orderClient.updateOrderStatus(orderId, "SUCCESS");            // from order client
        return "Payment is Successfully done and Order status updated.";
    }
}













//package com.cropdeal.paymentservice.service;
//
//import com.cropdeal.paymentservice.client.CropClient;
//import com.cropdeal.paymentservice.client.OrderClient;
//import com.cropdeal.paymentservice.dto.*;
//import com.cropdeal.paymentservice.repository.PaymentRepository;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.checkout.Session;
//import com.stripe.param.checkout.SessionCreateParams;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
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
//
//    public StripeService(PaymentRepository paymentRepository, CropClient cropClient, OrderClient orderClient) {
//        this.paymentRepository = paymentRepository;
//        this.cropClient = cropClient;
//        this.orderClient = orderClient;
//    }
//
//    private Crop crop;
//
//    // Creates a Stripe payment session for the crop purchase
//    public StripeResponse checkoutCrops(PurchaseRequest purchaseRequest) {
//        Stripe.apiKey = secretKey;
//
//        // Fetch crop details by name from Crop Service
//        Crop crop = cropClient.getCropByName(purchaseRequest.getCrop()).getBody();
//
//        if (crop == null) {
//            return StripeResponse.builder()
//                    .status("FAILURE")
//                    .message("Crop not found!")
//                    .build();
//        }
//
//        Long id = crop.getId(); // Extract crop ID
//
//        // Build Stripe product data
//        SessionCreateParams.LineItem.PriceData.ProductData productData =
//                SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                        .setName(purchaseRequest.getCrop())
//                        .build();
//
//        // Build Stripe price data
//        SessionCreateParams.LineItem.PriceData cropPrice =
//                SessionCreateParams.LineItem.PriceData.builder()
//                        .setCurrency(purchaseRequest.getCurrency() == null ? "USD" : purchaseRequest.getCurrency())
//                        .setUnitAmount(purchaseRequest.getAmount())
//                        .setProductData(productData)
//                        .build();
//
//        // Build Stripe line item
//        SessionCreateParams.LineItem lineItem =
//                SessionCreateParams.LineItem.builder()
//                        .setQuantity(purchaseRequest.getQuantity())
//                        .setPriceData(cropPrice)
//                        .build();
//
//        // Build Stripe session parameters
//        SessionCreateParams params =
//                SessionCreateParams.builder()
//                        .setMode(SessionCreateParams.Mode.PAYMENT)
//                        .setSuccessUrl("http://localhost:8085/payment/success/id/" + id + "/order/" + purchaseRequest.getOrderId())
//                        .setCancelUrl("http://localhost:8085/payment/cancel")
//                        .addLineItem(lineItem)
//                        .build();
//
//        Session session = null;
//
//        try {
//            session = Session.create(params);
//        } catch (StripeException e) {
//            System.err.println("Error creating Stripe session: " + e.getMessage());
//        }
//
//        if (session == null) {
//            return StripeResponse.builder()
//                    .status("FAILURE")
//                    .message("Failed to create payment session")
//                    .build();
//        }
//
//        // Save payment details to the database
//        paymentRepository.save(purchaseRequest);
//
//        return StripeResponse.builder()
//                .status("SUCCESS")
//                .message("Payment session created successfully")
//                .sessionId(session.getId())
//                .sessionUrl(session.getUrl())
//                .build();
//    }
//
//    // Deletes the crop after successful payment
//    public String deleteCropById(Long id) {
//        return cropClient.deleteCrop(id).getBody();
//    }
//
//    // NEW: Updates order status to SUCCESS after payment
//    public String handlePaymentSuccess(Long cropId, Long orderId) {
//        // Delete the crop
//        String result = cropClient.deleteCrop(cropId).getBody();
//
//        // Update order status to SUCCESS
//        orderClient.updateOrderStatus(orderId, "SUCCESS");
//
//        return "Payment is Successfully done and Order status updated.";
//    }
//}
