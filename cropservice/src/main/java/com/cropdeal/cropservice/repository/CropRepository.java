// Interface CropRepository to perform database operations
package com.cropdeal.cropservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cropdeal.cropservice.entity.Crop;

public interface CropRepository extends JpaRepository<Crop, Long> {
}
