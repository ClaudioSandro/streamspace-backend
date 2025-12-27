package com.streamspace.platform.spaces.domain.services;

import com.streamspace.platform.spaces.domain.model.commands.AddEquipmentCommand;
import com.streamspace.platform.spaces.domain.model.commands.RemoveEquipmentCommand;
import com.streamspace.platform.spaces.domain.model.commands.UpdateEquipmentCommand;
import com.streamspace.platform.spaces.domain.model.entities.Equipment;

import java.util.Optional;

public interface EquipmentCommandService {

    Optional<Equipment> handle(AddEquipmentCommand command);

    Optional<Equipment> handle(UpdateEquipmentCommand command);

    void handle(RemoveEquipmentCommand command);
}

