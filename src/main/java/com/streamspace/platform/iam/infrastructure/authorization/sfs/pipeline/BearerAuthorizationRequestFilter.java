package com.streamspace.platform.iam.infrastructure.authorization.sfs.pipeline;

import com.streamspace.platform.iam.infrastructure.authorization.sfs.model.UsernamePasswordAuthenticationTokenBuilder;
import com.streamspace.platform.iam.infrastructure.tokens.jwt.BearerTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class BearerAuthorizationRequestFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BearerAuthorizationRequestFilter.class);
    private final BearerTokenService tokenService;
    @Qualifier("defaultUserDetailsService")
    private final UserDetailsService userDetailsService;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/v1/auth",
            "/api/v1/authentication",
            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-resources",
            "/webjars"
    );

    public BearerAuthorizationRequestFilter(BearerTokenService bearerTokenService, UserDetailsService userDetailsService) {
        this.tokenService = bearerTokenService;
        this.userDetailsService = userDetailsService;
    }

    private boolean isPublicPath(String requestPath) {
        return PUBLIC_PATHS.stream().anyMatch(requestPath::startsWith);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return isPublicPath(path);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenService.getBearerTokenFrom(request);
            LOGGER.info("Token: {}", token);
            if (token != null && tokenService.validateToken(token)) {
                String username = tokenService.getUsernameFromToken(token);
                var userDetails = userDetailsService.loadUserByUsername(username);
                SecurityContextHolder.getContext()
                        .setAuthentication(UsernamePasswordAuthenticationTokenBuilder
                                .build(userDetails, request));
            } else {
                LOGGER.warn("Token is not valid or not present in the request.");

            }
        } catch (Exception e) {
            LOGGER.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
