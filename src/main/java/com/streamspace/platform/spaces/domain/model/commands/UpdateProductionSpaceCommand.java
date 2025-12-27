package com.streamspace.platform.spaces.domain.model.commands;

import java.math.BigDecimal;

public record UpdateProductionSpaceCommand(
        Long spaceId,
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

