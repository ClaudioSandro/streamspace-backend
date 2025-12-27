package com.streamspace.platform.spaces.application.internal.queryservices;

import com.streamspace.platform.spaces.domain.model.aggregates.ProductionSpace;
import com.streamspace.platform.spaces.domain.model.queries.*;
import com.streamspace.platform.spaces.domain.services.ProductionSpaceQueryService;
import com.streamspace.platform.spaces.infrastructure.persistence.jpa.repositories.ProductionSpaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionSpaceQueryServiceImpl implements ProductionSpaceQueryService {

    private final ProductionSpaceRepository productionSpaceRepository;

    public ProductionSpaceQueryServiceImpl(ProductionSpaceRepository productionSpaceRepository) {
        this.productionSpaceRepository = productionSpaceRepository;
    }

    @Override
    public Optional<ProductionSpace> handle(GetProductionSpaceByIdQuery query) {
        return productionSpaceRepository.findById(query.spaceId());
    }

    @Override
    public List<ProductionSpace> handle(GetAllProductionSpacesQuery query) {
        return productionSpaceRepository.findAll();
    }

    @Override
    public List<ProductionSpace> handle(GetProductionSpacesByOwnerIdQuery query) {
        return productionSpaceRepository.findByOwnerId(query.ownerId());
    }

    @Override
    public List<ProductionSpace> handle(GetProductionSpacesByTypeQuery query) {
        return productionSpaceRepository.findByType(query.type());
    }

    @Override
    public List<ProductionSpace> handle(GetProductionSpacesByCityQuery query) {
        return productionSpaceRepository.findByAddress_City(query.city());
    }

    @Override
    public List<ProductionSpace> handle(GetProductionSpacesByStatusQuery query) {
        return productionSpaceRepository.findByStatus(query.status());
    }
}
