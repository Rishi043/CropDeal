package com.cropdeal.orderservice.feign;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "crop-service",contextId = "CropClient",path="/crops")
public interface CropClient {

    // get crop by Id
    @GetMapping("/{id}")
    @Operation(summary = "Get crop by ID", description = "Finds a crop by its ID")
    public ResponseEntity<Crop> getCropById(@PathVariable Long id); // Can later replace with DTO

    //Get all crops
    @GetMapping ("/allCrops")
    @Operation(summary = "Get all crops", description = "Returns the list of all crops")
    public ResponseEntity<List<Crop>> getAllCrops();

    @GetMapping("/by-name/{name}")
    public ResponseEntity<Crop> getCropByName(@PathVariable String name);

    @GetMapping("/type/{type}")
    @Operation(summary = "Get crop by Type", description = "Finds a crop by its Type")
    public ResponseEntity<List<Crop>> getCropsFilteredByType(@PathVariable String type);

    // crops in Ascending order (by Price)
    @GetMapping("/filterPrice")
    public ResponseEntity<List<Crop>> getCropsSortedByPrice();

    @GetMapping("/filter")
    public ResponseEntity<List<Crop>> getCropsFiltered(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice);










}
