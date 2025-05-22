// CropService to handle business logic
package com.cropdeal.cropservice.service;

import com.cropdeal.cropservice.entity.Crop;
import com.cropdeal.cropservice.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CropService {

    @Autowired
    private CropRepository cropRepository;

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

    // Update existing crop
    public Crop updateCrop(Long id, Crop updatedCrop) {
        Optional<Crop> existingCrop = cropRepository.findById(id);
        if (existingCrop.isPresent()) {
            Crop crop = existingCrop.get();
            crop.setName(updatedCrop.getName());
            crop.setType(updatedCrop.getType());
            crop.setPrice(updatedCrop.getPrice());
            return cropRepository.save(crop);
        }
        return null;
    }

    // Delete crop by ID
    public boolean deleteCrop(Long id) {
        if (cropRepository.existsById(id)) {
            cropRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
