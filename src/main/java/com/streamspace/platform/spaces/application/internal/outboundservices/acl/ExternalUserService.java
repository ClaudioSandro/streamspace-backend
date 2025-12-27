package com.streamspace.platform.spaces.application.internal.outboundservices.acl;

import com.streamspace.platform.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

/**
 * ExternalUserService
 * <p>
 *     This class is an outbound service that allows the Spaces bounded context to interact with the IAM bounded context.
 *     It uses the IamContextFacade to communicate with the IAM context.
 * </p>
 */
@Service
public class ExternalUserService {

    private final IamContextFacade iamContextFacade;

    public ExternalUserService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Checks if a user with the given id exists.
     * @param userId The id of the user (owner).
     * @return true if the user exists, false otherwise.
     */
    public boolean existsUserById(Long userId) {
        return iamContextFacade.existsById(userId);
    }

    /**
     * Fetches the username of the user with the given id.
     * @param userId The id of the user.
     * @return The username of the user, or empty string if not found.
     */
    public String fetchUsernameByUserId(Long userId) {
        return iamContextFacade.fetchUsernameByUserId(userId);
    }

    /**
     * Fetches the id of the user with the given username.
     * @param username The username of the user.
     * @return The id of the user, or 0L if not found.
     */
    public Long fetchUserIdByUsername(String username) {
        return iamContextFacade.fetchUserIdByUsername(username);
    }
}

