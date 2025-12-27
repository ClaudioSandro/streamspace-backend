package com.streamspace.platform.spaces.interfaces.rest.resources;

import java.math.BigDecimal;

public record UpdateProductionSpaceResource(
        String name,
        String description,
        String type,
        String city,
        String district,
        String addressLine,
        BigDecimal hourlyRateAmount,
        String hourlyRateCurrency,
        int maxPeople,
        String rules
) {
}
