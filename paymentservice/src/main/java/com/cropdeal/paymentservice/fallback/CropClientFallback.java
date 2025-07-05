//package com.cropdeal.paymentservice.fallback;
//
//import com.cropdeal.paymentservice.client.CropClient;
//import com.cropdeal.paymentservice.dto.Crop;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CropClientFallback implements CropClient {
//
//    @Override
//    public ResponseEntity<Crop> getCropByName(String name) {
//        System.out.println("✅ Fallback triggered for getCropByName");
//        return ResponseEntity.ok(null);
//    }
//
//    @Override
//    public ResponseEntity<String> deleteCrop(Long id) {
//        System.out.println("Fallback: deleteCrop");
//        return ResponseEntity.ok("Fallback: Crop deletion failed");
//    }
//
//    @Override
//    public ResponseEntity<String> reduceAndDelete(Long cropId, int quantity) {
//        System.out.println("Fallback: reduceAndDelete");
//        return ResponseEntity.ok("Fallback: Crop stock update failed");
//    }
//
//    @Override
//    public ResponseEntity<Crop> getCropById(Long id) {
//        System.out.println("Fallback: getCropById");
//        return ResponseEntity.ok(null);
//    }
//}
