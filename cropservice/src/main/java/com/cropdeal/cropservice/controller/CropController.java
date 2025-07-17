// REST Controller CropController to handle API requests
package com.cropdeal.cropservice.controller;

import com.cropdeal.cropservice.entity.Crop;
import com.cropdeal.cropservice.service.CropService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cropdeal.cropservice.repository.CropRepository;

import java.util.List;

@RestController
@RequestMapping("/crops") // Base URL: http://localhost:8081/crops
@Tag(name = "Crop Management", description = "Crop Service APIs") // Swagger documentation
public class CropController {

    // Constructor-based dependency injection
    private final CropService cropService;

    @Autowired
    public CropController(CropService cropService) {
        this.cropService = cropService;
    }


    // Add a new crop with validation
    @PostMapping
    @Operation(summary = "Add a new crop", description = "Saves a new crop in the database")
    public ResponseEntity<Crop> addCrop(@Valid @RequestBody Crop crop) {
        Crop savedCrop = cropService.addCrop(crop);
        return ResponseEntity.ok(savedCrop);
    }

    // Get all crops
    @GetMapping ("/allCrops")
    @Operation(summary = "Get all crops", description = "Returns the list of all crops")
    public ResponseEntity<List<Crop>> getAllCrops() {
        return ResponseEntity.ok(cropService.getAllCrops());
    }

    // Get a single crop by ID (farmer)
    @GetMapping("/{id}")
    @Operation(summary = "Get crop by ID", description = "Finds a crop by its ID")
    public ResponseEntity<Crop> getCropById(@PathVariable Long id) {
        Crop crop = cropService.getCropById(id);
        if (crop != null) {
            return ResponseEntity.ok(crop);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update an existing crop (farmer)
    @PutMapping("/{id}")
    @Operation(summary = "Update crop", description = "Updates an existing crop by ID")
    public ResponseEntity<Crop> updateCrop(@PathVariable Long id, @Valid @RequestBody Crop crop) {
        Crop updated = cropService.updateCrop(id, crop);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/reorder-ids")
    public ResponseEntity<String> reorderCropIds() {
        cropService.reorderCropIds();
        return ResponseEntity.ok("Crop IDs reordered successfully!");
    }

    @GetMapping("/filterPrice")
    public ResponseEntity<List<Crop>> getCropsSortedByPrice() {
        return ResponseEntity.ok(cropService.getCropsSortedByPrice());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Crop>> getCropsFiltered(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(cropService.getCropsFiltered(minPrice, maxPrice));
    }

    @GetMapping("/by/{farmerMail}")
    public ResponseEntity<List<Crop>> getCropsByFarmer(@PathVariable String farmerMail) {
        return ResponseEntity.ok(cropService.getCropsByFarmerMail(farmerMail));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Crop>> getCropsFilteredByType(@PathVariable String type) {
        return ResponseEntity.ok(cropService.getCropsByType(type));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<List<Crop>> getCropsByName(@PathVariable String name) {
        List<Crop> crops = cropService.getCropsByName(name);
        if (crops.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(crops);
        }
        return ResponseEntity.ok(crops);
    }


    @PutMapping("/reduce-and-delete/{id}")
    public ResponseEntity<String> reduceAndDelete(@PathVariable Long id, @RequestParam int quantity) {
        cropService.reduceQuantityAndDeleteIfZero(id, quantity);
        return ResponseEntity.ok("Crop quantity updated. Deleted if quantity reached zero.");
    }




    // Delete crop by ID (farmer)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete crop", description = "Deletes a crop by ID")
    public ResponseEntity<String> deleteCrop(@PathVariable Long id) {
        boolean deleted = cropService.deleteCrop(id);
        if (deleted) {
            return ResponseEntity.ok("Crop deleted successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
