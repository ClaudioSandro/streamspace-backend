package com.streamspace.platform.spaces.interfaces.rest.transform;

import com.streamspace.platform.spaces.domain.model.commands.CreateProductionSpaceCommand;
import com.streamspace.platform.spaces.interfaces.rest.resources.CreateProductionSpaceResource;

public class CreateProductionSpaceCommandFromResourceAssembler {

    public static CreateProductionSpaceCommand toCommandFromResource(CreateProductionSpaceResource resource) {
        return new CreateProductionSpaceCommand(
                resource.ownerId(),
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
