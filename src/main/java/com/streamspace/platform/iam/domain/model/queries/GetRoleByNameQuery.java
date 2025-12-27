package com.streamspace.platform.iam.domain.model.queries;

import com.streamspace.platform.iam.domain.model.valueobjects.Roles;

public record GetRoleByNameQuery(Roles name) {
}
