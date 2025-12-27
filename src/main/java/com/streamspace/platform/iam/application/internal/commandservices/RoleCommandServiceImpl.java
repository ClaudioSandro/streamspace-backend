package com.streamspace.platform.iam.application.internal.commandservices;

import com.streamspace.platform.iam.domain.model.commands.SeedRolesCommand;
import com.streamspace.platform.iam.domain.model.entities.Role;
import com.streamspace.platform.iam.domain.model.valueobjects.Roles;
import com.streamspace.platform.iam.domain.services.RoleCommandService;
import com.streamspace.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RoleCommandServiceImpl implements RoleCommandService {
    private final RoleRepository roleRepository;

    public RoleCommandServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void handle(SeedRolesCommand command) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(Roles.valueOf(role.name())));
            }
        });
    }
}
