// CropService to handle business logic
package com.cropdeal.cropservice.service;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import com.cropdeal.cropservice.entity.Crop;
import com.cropdeal.cropservice.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CropService {

    @Autowired
    private CropRepository cropRepository;

    public CropService(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    // Get all crops
    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }

    // Get crop by ID
    public Crop getCropById(Long id) {
        return cropRepository.findById(id).orElse(null);
    }

    // Add new crop
    public Crop addCrop(Crop crop) {
        crop.setId(null);  // Ensure ID is null so it's auto-generated
        return cropRepository.save(crop);
    }

    // Sort by Prices
    public List<Crop> getCropsSortedByPrice() {
        return cropRepository.findAll(Sort.by(Sort.Direction.ASC, "pricePerKg"));
    }


    // Sort Prices by Range of Price
    public List<Crop> getCropsFiltered(Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return cropRepository.findByPricePerKgBetween(minPrice, maxPrice, Sort.by(Sort.Direction.ASC, "pricePerKg"));
        } else {
            return cropRepository.findAll(); // No sorting when filter isn't applied
        }
    }

    // Update existing crop
    public Crop updateCrop(Long id, Crop updatedCrop) {
        Optional<Crop> existingCrop = cropRepository.findById(id);
        if (existingCrop.isPresent()) {
            Crop crop = existingCrop.get();
            crop.setName(updatedCrop.getName());
            crop.setType(updatedCrop.getType());
            //crop.setPrice(updatedCrop.getPrice());
            return cropRepository.save(crop);
        }
        return null;
    }

    // Delete crop by ID
    @Modifying
    @Transactional
    public boolean deleteCrop(Long id) {
        if (cropRepository.existsById(id)) {
            cropRepository.deleteById(id);
            cropRepository.reorderCropIds(); // Auto-reorder after deletion
            return true;
        }
        return false;
    }

    @Transactional
    public void reorderCropIds() {

        cropRepository.reorderCropIds();
    }

    public List<Crop> getCropsByType(String type) {

        return cropRepository.findByType(type);
    }

    public List<Crop> getCropsByFarmerMail(String farmerMail) {

        return cropRepository.findByFarmerMail(farmerMail);
    }

    public Crop getCropByName(String name) {
        List<Crop> crops = cropRepository.findByName(name);
        return crops.isEmpty() ? null : crops.get(0);
    }

    public void reduceQuantityAndDeleteIfZero(Long cropId, int quantityToDeduct) {
        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + cropId));

        if (crop.getTotalQuantity() < quantityToDeduct) {
            throw new RuntimeException("Not enough quantity available.");
        }

        crop.setTotalQuantity(crop.getTotalQuantity() - quantityToDeduct);

        if (crop.getTotalQuantity() == 0) {
            cropRepository.delete(crop);
        } else {
            cropRepository.save(crop);
        }
    }
}