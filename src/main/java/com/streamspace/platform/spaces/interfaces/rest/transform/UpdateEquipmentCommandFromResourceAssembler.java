package com.streamspace.platform.spaces.interfaces.rest.transform;

import com.streamspace.platform.spaces.domain.model.commands.UpdateEquipmentCommand;
import com.streamspace.platform.spaces.interfaces.rest.resources.UpdateEquipmentResource;

public class UpdateEquipmentCommandFromResourceAssembler {

    public static UpdateEquipmentCommand toCommandFromResource(Long spaceId, Long equipmentId, UpdateEquipmentResource resource) {
        return new UpdateEquipmentCommand(
                spaceId,
                equipmentId,
                resource.name(),
                resource.quantity()
        );
    }
}

