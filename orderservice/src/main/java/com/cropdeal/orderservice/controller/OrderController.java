// OrderController.java
package com.cropdeal.orderservice.controller;

import com.cropdeal.orderservice.dto.OrderRequestDTO;
import com.cropdeal.orderservice.entity.Order;
import com.cropdeal.orderservice.feign.Crop;
import com.cropdeal.orderservice.feign.CropClient;
import com.cropdeal.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping ("/allOrders")
    public ResponseEntity<List<Order>> getAllOrdersOfDealer() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping ("/OrderId/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping ("/placeOrder")
    public ResponseEntity<String> placeOrder(@Valid @RequestBody OrderRequestDTO dto) {
        Order placedOrder = orderService.placeOrder(dto);
        String message = "Order placed successfully with Order ID: " + placedOrder.getOrderId();
        return ResponseEntity.ok(message);

        // String message = "Order placed successfully with Crop ID: " + placedOrder.getCropId();

    }

    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return "Order deleted successfully";
    }

    // ***********  Using Crop Feign Client **********************

    @GetMapping("/crops/{id}")
    public ResponseEntity<Crop> getCropById(@PathVariable Long id)
    {
        return ResponseEntity.ok(cropClient.getCropById(id).getBody());

    }

    // Get all crops
    @GetMapping ("/crops")
    @Operation(summary = "Get all crops", description = "Returns the list of all crops")
    public ResponseEntity<List<Crop>> getAllCrops() {
        return ResponseEntity.ok(cropClient.getAllCrops().getBody());
    }

    // Get crop by Name
    @GetMapping("/by-name/{name}")
    public ResponseEntity<Crop> getCropByName(@PathVariable String name){
        return ResponseEntity.ok(cropClient.getCropByName(name).getBody());
    }

    // Get crop by Type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Crop>> getCropsFilteredByType(@PathVariable String type) {
        return ResponseEntity.ok(cropClient.getCropsFilteredByType(type).getBody());
    }

    // crops in Ascending order (by Price)
    @GetMapping("/filterPrice")
    public ResponseEntity<List<Crop>> getCropsSortedByPrice(){
        return ResponseEntity.ok(cropClient.getCropsSortedByPrice().getBody());
    }

    // get crops by price in Range
    @GetMapping("/filter")
    public ResponseEntity<List<Crop>> getCropsFiltered(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice){
        return ResponseEntity.ok(cropClient.getCropsFiltered(minPrice, maxPrice).getBody());
    }

    // New endpoint to get orders by dealer email
    @GetMapping("/dealer/{email}")
    public List<Order> getByDealer(@PathVariable String email) {
        return orderService.getOrdersByDealerEmail(email);
    }

    @PutMapping("/updateStatus/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        orderService.updateOrderStatusByCropId(orderId, status);
        return "Order status updated to " + status;
    }

}
