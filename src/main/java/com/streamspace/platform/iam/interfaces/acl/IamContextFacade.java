package com.streamspace.platform.iam.interfaces.acl;

import com.streamspace.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.streamspace.platform.iam.domain.model.queries.GetUserByUsernameQuery;
import com.streamspace.platform.iam.domain.services.UserCommandService;
import com.streamspace.platform.iam.domain.services.UserQueryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class IamContextFacade {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public IamContextFacade(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    public Long fetchUserIdByUsername(String username) {
        var getUserByUsernameQuery = new GetUserByUsernameQuery(username);
        var result = userQueryService.handle(getUserByUsernameQuery);
        if (result.isEmpty()) return 0L;
        return result.get().getId();
    }

    public String fetchUsernameByUserId(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var result = userQueryService.handle(getUserByIdQuery);
        if (result.isEmpty()) return StringUtils.EMPTY;
        return result.get().getUsername();
    }

    public boolean existsById(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var result = userQueryService.handle(getUserByIdQuery);
        return result.isPresent();
    }

    public boolean existsByUsername(String username) {
        var getUserByUsernameQuery = new GetUserByUsernameQuery(username);
        var result = userQueryService.handle(getUserByUsernameQuery);
        return result.isPresent();
    }
}

