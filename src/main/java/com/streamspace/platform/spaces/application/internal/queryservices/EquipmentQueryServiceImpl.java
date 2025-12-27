package com.streamspace.platform.spaces.application.internal.queryservices;

import com.streamspace.platform.spaces.domain.model.entities.Equipment;
import com.streamspace.platform.spaces.domain.model.queries.GetEquipmentBySpaceIdQuery;
import com.streamspace.platform.spaces.domain.services.EquipmentQueryService;
import com.streamspace.platform.spaces.infrastructure.persistence.jpa.repositories.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentQueryServiceImpl implements EquipmentQueryService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentQueryServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public List<Equipment> handle(GetEquipmentBySpaceIdQuery query) {
        return equipmentRepository.findByProductionSpaceId(query.spaceId());
    }
}

