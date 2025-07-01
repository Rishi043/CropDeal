package com.cropdeal.cropservice.controller;

import com.cropdeal.cropservice.entity.Crop;
import com.cropdeal.cropservice.service.CropService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CropControllerTest {

    // Mock the repository to avoid hitting the real database
    @Mock
    private CropService cropService;

    // Inject mocked service into crop Controller
    @InjectMocks
    private CropController cropController;

    private Crop crop;

    // Setting up a sample Crop object before each test
    @BeforeEach
    void setUp() {
        // to initialize Mockito (initializing crop object)
        MockitoAnnotations.openMocks(this);

        crop = new Crop();
        crop.setId(1L);
        crop.setName("Rice");
        crop.setType("Kharif");
        crop.setFarmerMail("farmer@example.com");
        crop.setPricePerKg(2200.0);
        crop.setTotalQuantity(100);
    }

    // testing - updated Crop
    @Test
    void testUpdateCrop() {
        Long cropId = 1L;
        when(cropService.updateCrop(eq(cropId), any(Crop.class))).thenReturn(crop);

        ResponseEntity<Crop> response = cropController.updateCrop(cropId, crop);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Rice", response.getBody().getName());
        verify(cropService, times(1)).updateCrop(cropId, crop);
    }

    // testing - add Crop
    @Test
    void testAddCrop() {
        when(cropService.addCrop(any(Crop.class))).thenReturn(crop);
        ResponseEntity<Crop> response = cropController.addCrop(crop);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Rice", response.getBody().getName());
    }

    // testing - crop by Id
    @Test
    void testGetCropById_Found() {

        when(cropService.getCropById(1L)).thenReturn(crop);
        ResponseEntity<Crop> response = cropController.getCropById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Rice", response.getBody().getName());
        verify(cropService, times(1)).getCropById(1L);
    }

    @Test
    void testGetCropById_NotFound() {
        when(cropService.getCropById(99L)).thenReturn(null);
        ResponseEntity<Crop> response = cropController.getCropById(99L);
        assertEquals(404, response.getStatusCodeValue());
        verify(cropService, times(1)).getCropById(99L);
    }

    // testing - delete Crop
    @Test
    void testDeleteCrop_Success() {
        when(cropService.deleteCrop(1L)).thenReturn(true);
        ResponseEntity<String> response = cropController.deleteCrop(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Crop deleted successfully!", response.getBody());
        verify(cropService, times(1)).deleteCrop(1L);
    }

    // testing - get all Crops
    @Test
    void testGetAllCrops() {
        when(cropService.getAllCrops()).thenReturn(List.of(crop));
        ResponseEntity<List<Crop>> response = cropController.getAllCrops();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(cropService, times(1)).getAllCrops();
    }

    // testing - crop by Type
    @Test
    void testGetCropsByType() {
        when(cropService.getCropsByType("Kharif")).thenReturn(List.of(crop));
        ResponseEntity<List<Crop>> response = cropController.getCropsFilteredByType("Kharif");
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Kharif", response.getBody().get(0).getType());
        verify(cropService, times(1)).getCropsByType("Kharif");
    }


    // testing - by mailId
    @Test
    void testGetCropsByFarmerMail() {
        String farmerMail = "farmer@example.com";
        when(cropService.getCropsByFarmerMail(farmerMail)).thenReturn(List.of(crop));

        ResponseEntity<List<Crop>> response = cropController.getCropsByFarmer(farmerMail);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(farmerMail, response.getBody().get(0).getFarmerMail());
        verify(cropService, times(1)).getCropsByFarmerMail(farmerMail);
    }


}
