package com.streamspace.platform.spaces.interfaces.rest.resources;

import java.math.BigDecimal;

public record ProductionSpaceResource(
        Long id,
        Long ownerId,
        String name,
        String description,
        String type,
        String status,
        String city,
        String district,
        String addressLine,
        BigDecimal hourlyRateAmount,
        String hourlyRateCurrency,
        int maxPeople,
        String rules
) {
}
