package com.streamspace.platform.spaces.infrastructure.persistence.jpa.repositories;

import com.streamspace.platform.spaces.domain.model.aggregates.ProductionSpace;
import com.streamspace.platform.spaces.domain.model.valueobjects.SpaceStatus;
import com.streamspace.platform.spaces.domain.model.valueobjects.SpaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionSpaceRepository extends JpaRepository<ProductionSpace, Long> {

    List<ProductionSpace> findByOwnerId(Long ownerId);

    List<ProductionSpace> findByStatus(SpaceStatus status);

    List<ProductionSpace> findByType(SpaceType type);

    List<ProductionSpace> findByTypeAndStatus(SpaceType type, SpaceStatus status);

    List<ProductionSpace> findByAddress_City(String city);

    List<ProductionSpace> findByAddress_CityAndStatus(String city, SpaceStatus status);

    boolean existsByOwnerIdAndName(Long ownerId, String name);
}

