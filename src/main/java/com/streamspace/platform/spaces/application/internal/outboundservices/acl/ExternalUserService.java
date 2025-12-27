package com.streamspace.platform.spaces.application.internal.outboundservices.acl;

import com.streamspace.platform.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

@Service
public class ExternalUserService {

    private final IamContextFacade iamContextFacade;

    public ExternalUserService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public boolean existsUserById(Long userId) {
        return iamContextFacade.existsById(userId);
    }

    public String fetchUsernameByUserId(Long userId) {
        return iamContextFacade.fetchUsernameByUserId(userId);
    }

    public Long fetchUserIdByUsername(String username) {
        return iamContextFacade.fetchUserIdByUsername(username);
    }
}

