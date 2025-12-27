package com.streamspace.platform.iam.infrastructure.hashing.bcrypt;

import com.streamspace.platform.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BCryptHashingService extends HashingService, PasswordEncoder {
}
