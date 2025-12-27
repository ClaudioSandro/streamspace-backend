package com.streamspace.platform.iam.interfaces.rest.transform;

import com.streamspace.platform.iam.domain.model.entities.Role;
import com.streamspace.platform.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role entity) {
        return new RoleResource(entity.getId(), entity.getStringName());

    }
}
