package com.streamspace.platform.iam.domain.services;

import com.streamspace.platform.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}
