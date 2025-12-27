package com.streamspace.platform.spaces.interfaces.rest.resources;

public record EquipmentResource(
        Long id,
        Long spaceId,
        String name,
        int quantity
) {
}

