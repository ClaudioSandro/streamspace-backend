package com.streamspace.platform.spaces.interfaces.rest.transform;

import com.streamspace.platform.spaces.domain.model.entities.Equipment;
import com.streamspace.platform.spaces.interfaces.rest.resources.EquipmentResource;

public class EquipmentResourceFromEntityAssembler {

    public static EquipmentResource toResourceFromEntity(Equipment entity) {
        return new EquipmentResource(
                entity.getId(),
                entity.getProductionSpace().getId(),
                entity.getName(),
                entity.getQuantity()
        );
    }
}

