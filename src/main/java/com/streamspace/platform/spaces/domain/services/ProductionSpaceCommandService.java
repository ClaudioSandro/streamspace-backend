package com.streamspace.platform.spaces.domain.services;

import com.streamspace.platform.spaces.domain.model.aggregates.ProductionSpace;
import com.streamspace.platform.spaces.domain.model.commands.*;

import java.util.Optional;

public interface ProductionSpaceCommandService {

    Optional<ProductionSpace> handle(CreateProductionSpaceCommand command);

    Optional<ProductionSpace> handle(UpdateProductionSpaceCommand command);

    void handle(DeleteProductionSpaceCommand command);

    Optional<ProductionSpace> handle(ActivateProductionSpaceCommand command);

    Optional<ProductionSpace> handle(DeactivateProductionSpaceCommand command);
}

