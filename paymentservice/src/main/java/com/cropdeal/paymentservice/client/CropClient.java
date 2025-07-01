package com.cropdeal.paymentservice.client;

import com.cropdeal.paymentservice.dto.Crop;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "crop-service", contextId = "CropClient",path = "/crops")

public interface CropClient {

    @GetMapping("/by-name/{name}")
    public ResponseEntity<Crop> getCropByName(@PathVariable String name);

    // Delete crop by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCrop(@PathVariable Long id);

    @PutMapping("/reduce-and-delete/{id}")
    ResponseEntity<String> reduceAndDelete(@PathVariable("id") Long cropId,
                                           @RequestParam("quantity") int quantity);


}
