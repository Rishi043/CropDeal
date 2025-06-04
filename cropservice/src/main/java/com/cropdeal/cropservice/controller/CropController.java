// REST Controller CropController to handle API requests
package com.cropdeal.cropservice.controller;

import com.cropdeal.cropservice.entity.Crop;
import com.cropdeal.cropservice.service.CropService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crops") // Base URL: http://localhost:8081/crops
@Tag(name = "Crop Management", description = "Crop Service APIs") // Swagger documentation
public class CropController {

    // Constructor-based dependency injection
    private final CropService cropService;

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
    @GetMapping
    @Operation(summary = "Get all crops", description = "Returns the list of all crops")
    public ResponseEntity<List<Crop>> getAllCrops() {
        return ResponseEntity.ok(cropService.getAllCrops());
    }

    // Get a single crop by ID
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

    // Update an existing crop
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


    @GetMapping("/filter")
    public ResponseEntity<List<Crop>> getCropsFiltered(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(cropService.getCropsFiltered(minPrice, maxPrice));
    }


    // Delete crop by ID
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
