// REST Controller CropController to handle API requests
package com.cropdeal.cropservice.controller;

import com.cropdeal.cropservice.entity.Crop;
import com.cropdeal.cropservice.service.CropService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crops") // Base URL for all endpoints in this controller
@Tag(name = "Crop Management", description = "Crop Service APIs")      // Swagger Tag
public class CropController {

    private final CropService cropService;

    // Constructor-based dependency injection
    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    // Add a new crop
    @PostMapping
    @Operation(summary = "Add a new crop", description = "Saves a new crop in the database")
    public Crop addCrop(@RequestBody Crop crop) {
        return cropService.addCrop(crop);
    }

    // Get all crops
    @GetMapping
    @Operation(summary = "Get all crops", description = "Returns the list of all crops")
    public List<Crop> getAllCrops() {
        return cropService.getAllCrops();
    }

    // Get a single crop by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get crop by ID", description = "Finds a crop by its ID")
    public Crop getCropById(@PathVariable Long id) {
        return cropService.getCropById(id);
    }

    // Update an existing crop by ID
    @PutMapping("/{id}")
    @Operation(summary = "Update crop", description = "Updates an existing crop by ID")
    public Crop updateCrop(@PathVariable Long id, @RequestBody Crop crop) {
        return cropService.updateCrop(id, crop);
    }

    // Delete a crop by ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete crop", description = "Deletes a crop by ID")
    public String deleteCrop(@PathVariable Long id) {
        cropService.deleteCrop(id);
        return "Crop deleted successfully!";
    }
}
