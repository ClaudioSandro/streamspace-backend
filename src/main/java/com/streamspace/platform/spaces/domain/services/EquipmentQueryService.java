package com.streamspace.platform.spaces.domain.services;

import com.streamspace.platform.spaces.domain.model.entities.Equipment;
import com.streamspace.platform.spaces.domain.model.queries.GetEquipmentBySpaceIdQuery;

import java.util.List;

public interface EquipmentQueryService {

    List<Equipment> handle(GetEquipmentBySpaceIdQuery query);
}

