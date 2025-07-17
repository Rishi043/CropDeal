package com.cropdeal.orderservice.controller;

import com.cropdeal.orderservice.dto.OrderRequestDTO;
import com.cropdeal.orderservice.dto.OrderResponseDTO;
import com.cropdeal.orderservice.entity.Order;
import com.cropdeal.orderservice.feign.Crop;
import com.cropdeal.orderservice.feign.CropClient;
import com.cropdeal.orderservice.service.JwtService;
import com.cropdeal.orderservice.service.OrderService;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CropClient cropClient;

    @Autowired
    private JwtService jwtService;

    // ✅ Show only logged-in dealer's orders with crop name
    @GetMapping("/dealer/orders")
    public ResponseEntity<List<OrderResponseDTO>> getDealerOrders(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        return ResponseEntity.ok(orderService.getOrdersByDealerEmailWithCropName(email));
    }

    // ✅ Place order with cropId and quantity only
    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody OrderRequestDTO dto) {

        String email = jwtService.extractUsername(token.replace("Bearer ", ""));

        Crop crop;

        try {
            crop = cropClient.getCropById(dto.getCropId()).getBody();
        } catch (FeignException.NotFound ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Crop with ID " + dto.getCropId() + " not found.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching crop details: " + e.getMessage());
        }

        if (dto.getQuantity() > crop.getTotalQuantity()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Not enough quantity available. Only " + crop.getTotalQuantity() + "kg left.");
        }

        Order placedOrder = orderService.placeOrder(dto, email);
        return ResponseEntity.ok("✅ Order placed successfully with Order ID: " + placedOrder.getOrderId());
    }



    // ✅ Other endpoints remain as-is
    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return "Order deleted successfully";
    }

    @GetMapping("/OrderId/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/updateStatus/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        orderService.updateOrderStatusByCropId(orderId, status);
        return "Order status updated to " + status;
    }

    // 🌾 Crop APIs via Feign
    @GetMapping("/crops/{id}")
    public ResponseEntity<Crop> getCropById(@PathVariable Long id) {
        return ResponseEntity.ok(cropClient.getCropById(id).getBody());
    }

    @GetMapping("/crops")
    @Operation(summary = "Get all crops")
    public ResponseEntity<List<Crop>> getAllCrops() {
        return ResponseEntity.ok(cropClient.getAllCrops().getBody());
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<Crop> getCropByName(@PathVariable String name) {
        return ResponseEntity.ok(cropClient.getCropByName(name).getBody());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Crop>> getCropsFilteredByType(@PathVariable String type) {
        return ResponseEntity.ok(cropClient.getCropsFilteredByType(type).getBody());
    }

    @GetMapping("/filterPrice")
    public ResponseEntity<List<Crop>> getCropsSortedByPrice() {
        return ResponseEntity.ok(cropClient.getCropsSortedByPrice().getBody());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Crop>> getCropsFiltered(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(cropClient.getCropsFiltered(minPrice, maxPrice).getBody());
    }
}
