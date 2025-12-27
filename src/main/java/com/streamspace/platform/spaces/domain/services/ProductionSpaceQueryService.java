package com.streamspace.platform.spaces.domain.services;

import com.streamspace.platform.spaces.domain.model.aggregates.ProductionSpace;
import com.streamspace.platform.spaces.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface ProductionSpaceQueryService {

    Optional<ProductionSpace> handle(GetProductionSpaceByIdQuery query);

    List<ProductionSpace> handle(GetAllProductionSpacesQuery query);

    List<ProductionSpace> handle(GetProductionSpacesByOwnerIdQuery query);

    List<ProductionSpace> handle(GetProductionSpacesByTypeQuery query);

    List<ProductionSpace> handle(GetProductionSpacesByCityQuery query);

    List<ProductionSpace> handle(GetProductionSpacesByStatusQuery query);
}

