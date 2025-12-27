package com.streamspace.platform.spaces.domain.model.queries;

import com.streamspace.platform.spaces.domain.model.valueobjects.SpaceStatus;

public record GetProductionSpacesByStatusQuery(SpaceStatus status) {
}

