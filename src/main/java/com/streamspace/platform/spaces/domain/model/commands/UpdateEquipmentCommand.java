package com.streamspace.platform.spaces.domain.model.commands;

public record UpdateEquipmentCommand(Long spaceId, Long equipmentId, String name, int quantity) {
}

