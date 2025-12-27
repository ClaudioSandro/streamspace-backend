package com.streamspace.platform.spaces.interfaces.rest.transform;

import com.streamspace.platform.spaces.domain.model.aggregates.ProductionSpace;
import com.streamspace.platform.spaces.interfaces.rest.resources.ProductionSpaceResource;

public class ProductionSpaceResourceFromEntityAssembler {

    public static ProductionSpaceResource toResourceFromEntity(ProductionSpace entity) {
        return toResourceFromEntity(entity, null);
    }

    public static ProductionSpaceResource toResourceFromEntity(ProductionSpace entity, String imageUrl) {
        return new ProductionSpaceResource(
                entity.getId(),
                entity.getOwnerId(),
                entity.getName(),
                entity.getDescription(),
                entity.getType().name(),
                entity.getStatus().name(),
                entity.getAddress().city(),
                entity.getAddress().district(),
                entity.getAddress().addressLine(),
                entity.getHourlyRate().amount(),
                entity.getHourlyRate().currency(),
                entity.getMaxPeople(),
                entity.getRules(),
                imageUrl
        );
    }
}

