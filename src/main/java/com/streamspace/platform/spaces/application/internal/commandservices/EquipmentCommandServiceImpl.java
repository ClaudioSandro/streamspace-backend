package com.streamspace.platform.spaces.application.internal.commandservices;

import com.streamspace.platform.spaces.domain.model.commands.AddEquipmentCommand;
import com.streamspace.platform.spaces.domain.model.commands.RemoveEquipmentCommand;
import com.streamspace.platform.spaces.domain.model.commands.UpdateEquipmentCommand;
import com.streamspace.platform.spaces.domain.model.entities.Equipment;
import com.streamspace.platform.spaces.domain.services.EquipmentCommandService;
import com.streamspace.platform.spaces.infrastructure.persistence.jpa.repositories.ProductionSpaceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EquipmentCommandServiceImpl implements EquipmentCommandService {

    private final ProductionSpaceRepository productionSpaceRepository;

    public EquipmentCommandServiceImpl(ProductionSpaceRepository productionSpaceRepository) {
        this.productionSpaceRepository = productionSpaceRepository;
    }

    @Override
    public Optional<Equipment> handle(AddEquipmentCommand command) {
        var spaceId = command.spaceId();
        var result = productionSpaceRepository.findById(spaceId);

        if (result.isEmpty()) {
            throw new IllegalArgumentException("Production space with id " + spaceId + " does not exist");
        }

        var productionSpace = result.get();
        var equipment = productionSpace.addEquipment(command.name(), command.quantity());
        var savedSpace = productionSpaceRepository.save(productionSpace);

        return savedSpace.getEquipment().stream()
                .filter(e -> e.getName().equals(command.name()))
                .findFirst();
    }

    @Override
    public Optional<Equipment> handle(UpdateEquipmentCommand command) {
        var spaceId = command.spaceId();
        var result = productionSpaceRepository.findById(spaceId);

        if (result.isEmpty()) {
            throw new IllegalArgumentException("Production space with id " + spaceId + " does not exist");
        }

        var productionSpace = result.get();
        productionSpace.updateEquipment(command.equipmentId(), command.name(), command.quantity());
        productionSpaceRepository.save(productionSpace);

        return productionSpace.getEquipment().stream()
                .filter(e -> e.getId().equals(command.equipmentId()))
                .findFirst();
    }

    @Override
    public void handle(RemoveEquipmentCommand command) {
        var spaceId = command.spaceId();
        var result = productionSpaceRepository.findById(spaceId);

        if (result.isEmpty()) {
            throw new IllegalArgumentException("Production space with id " + spaceId + " does not exist");
        }

        var productionSpace = result.get();
        productionSpace.removeEquipment(command.equipmentId());
        productionSpaceRepository.save(productionSpace);
    }
}

