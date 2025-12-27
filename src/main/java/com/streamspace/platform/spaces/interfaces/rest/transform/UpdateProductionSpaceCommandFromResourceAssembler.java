package com.streamspace.platform.spaces.interfaces.rest.transform;

import com.streamspace.platform.spaces.domain.model.commands.UpdateProductionSpaceCommand;
import com.streamspace.platform.spaces.interfaces.rest.resources.UpdateProductionSpaceResource;

public class UpdateProductionSpaceCommandFromResourceAssembler {

    public static UpdateProductionSpaceCommand toCommandFromResource(Long spaceId, UpdateProductionSpaceResource resource) {
        return new UpdateProductionSpaceCommand(
                spaceId,
                resource.name(),
                resource.description(),
                resource.type(),
                resource.city(),
                resource.district(),
                resource.addressLine(),
                resource.hourlyRateAmount(),
                resource.hourlyRateCurrency(),
                resource.maxPeople(),
                resource.rules()
        );
    }
}
