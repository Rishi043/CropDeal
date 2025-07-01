package com.cropdeal.cropservice.service;

import com.cropdeal.cropservice.entity.Crop;
import com.cropdeal.cropservice.repository.CropRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Enable Mockito for unit testing
@ExtendWith(MockitoExtension.class)
class CropServiceTest {

    // Mock the repository to avoid hitting the real database
    // create fake objects
    @Mock
    private CropRepository cropRepository;

    // Inject mocked service into crop Service
    @InjectMocks
    private CropService cropService;

    // Test for adding a crop
    @Test
    void testAddCrop_shouldReturnSavedCrop() {
        Crop crop = new Crop();
        crop.setFarmerMail("farmer2@example.com");
        crop.setName("Wheat");
        crop.setType("Grain");
        crop.setPricePerKg(120.0);
        crop.setTotalQuantity(10);

        // Mock repository save behavior
        when(cropRepository.save(any(Crop.class))).thenReturn(crop);

        // Call the service method
        Crop result = cropService.addCrop(crop);

        // Assertions
        assertNotNull(result);
        assertEquals("Wheat", result.getName());
        verify(cropRepository).save(any(Crop.class));
    }

    // Test for retrieving all crops
    @Test
    void testGetAllCrops_shouldReturnList() {
        List<Crop> crops = Arrays.asList(new Crop(), new Crop());
        when(cropRepository.findAll()).thenReturn(crops);

        List<Crop> result = cropService.getAllCrops();

        assertEquals(2, result.size());
        verify(cropRepository).findAll();
    }

    // Test for retrieving crops by farmer's email
    @Test
    void testGetCropsByFarmer_shouldReturnCrops() {
        String farmerMail = "farmer@example.com";
        List<Crop> crops = Arrays.asList(new Crop(), new Crop());
        when(cropRepository.findByFarmerMail(farmerMail)).thenReturn(crops);

        List<Crop> result = cropService.getCropsByFarmerMail(farmerMail);

        assertEquals(2, result.size());
        verify(cropRepository).findByFarmerMail(farmerMail);
    }

    // Test for retrieving crop by name
    @Test
    void testGetCropsByCropID_shouldReturnCrops() {
        String cropName = "Wheat";
        List<Crop> crops = Arrays.asList(new Crop());
        when(cropRepository.findByName(cropName)).thenReturn(crops);

        Crop result = cropService.getCropByName(cropName);

        assertNotNull(result);
        verify(cropRepository).findByName(cropName);
    }
}
