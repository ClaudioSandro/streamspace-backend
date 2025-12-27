package com.streamspace.platform.spaces.interfaces.rest.transform;

import com.streamspace.platform.spaces.domain.model.commands.AddEquipmentCommand;
import com.streamspace.platform.spaces.interfaces.rest.resources.AddEquipmentResource;

public class AddEquipmentCommandFromResourceAssembler {

    public static AddEquipmentCommand toCommandFromResource(Long spaceId, AddEquipmentResource resource) {
        return new AddEquipmentCommand(
                spaceId,
                resource.name(),
                resource.quantity()
        );
    }
}

