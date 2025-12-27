package com.streamspace.platform.iam.interfaces.acl;

import com.streamspace.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.streamspace.platform.iam.domain.model.queries.GetUserByUsernameQuery;
import com.streamspace.platform.iam.domain.services.UserCommandService;
import com.streamspace.platform.iam.domain.services.UserQueryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * IamContextFacade
 * <p>
 *     This class is a facade for the IAM context. It provides a simple interface for other bounded contexts to interact with the
 *     IAM context.
 *     This class is a part of the ACL layer.
 * </p>
 */
@Service
public class IamContextFacade {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public IamContextFacade(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    /**
     * Fetches the id of the user with the given username.
     * @param username The username of the user.
     * @return The id of the user, or 0L if not found.
     */
    public Long fetchUserIdByUsername(String username) {
        var getUserByUsernameQuery = new GetUserByUsernameQuery(username);
        var result = userQueryService.handle(getUserByUsernameQuery);
        if (result.isEmpty()) return 0L;
        return result.get().getId();
    }

    /**
     * Fetches the username of the user with the given id.
     * @param userId The id of the user.
     * @return The username of the user, or empty string if not found.
     */
    public String fetchUsernameByUserId(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var result = userQueryService.handle(getUserByIdQuery);
        if (result.isEmpty()) return StringUtils.EMPTY;
        return result.get().getUsername();
    }

    /**
     * Checks if a user with the given id exists.
     * @param userId The id of the user.
     * @return true if the user exists, false otherwise.
     */
    public boolean existsById(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var result = userQueryService.handle(getUserByIdQuery);
        return result.isPresent();
    }

    /**
     * Checks if a user with the given username exists.
     * @param username The username of the user.
     * @return true if the user exists, false otherwise.
     */
    public boolean existsByUsername(String username) {
        var getUserByUsernameQuery = new GetUserByUsernameQuery(username);
        var result = userQueryService.handle(getUserByUsernameQuery);
        return result.isPresent();
    }
}

