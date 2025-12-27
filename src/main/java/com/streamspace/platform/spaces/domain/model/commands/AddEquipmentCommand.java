package com.streamspace.platform.spaces.domain.model.commands;

public record AddEquipmentCommand(Long spaceId, String name, int quantity) {
}

