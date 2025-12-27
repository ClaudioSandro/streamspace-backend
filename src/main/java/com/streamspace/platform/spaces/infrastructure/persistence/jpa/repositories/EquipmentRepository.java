package com.streamspace.platform.spaces.infrastructure.persistence.jpa.repositories;

import com.streamspace.platform.spaces.domain.model.entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByProductionSpaceId(Long productionSpaceId);

    boolean existsByProductionSpaceIdAndName(Long productionSpaceId, String name);
}

