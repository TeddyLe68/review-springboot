package com.teddy.youtuberef.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityProblemSupport implements AuthenticationEntryPoint, AccessDeniedHandler {

    /**
     * This method is called when an unauthenticated user tries to access a protected resource.
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     *
     * SecurityUtils.responseFailCredential is a utility method that sends a JSON response with the given status and message.
     */
    @Override
    public void commence( final HttpServletRequest request, final HttpServletResponse response,final AuthenticationException authException) throws IOException, ServletException {
        SecurityUtils.responseFailCredential(response, HttpStatus.UNAUTHORIZED, authException.getLocalizedMessage());
    }

    /**
     * This method is called when an authenticated user tries to access a resource they don't have permission to access.
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     *
     * SecurityUtils.responseFailCredential is a utility method that sends a JSON response with the given status and message.
     */
    @Override
    public void handle(final HttpServletRequest request,final HttpServletResponse response,final AccessDeniedException accessDeniedException) throws IOException, ServletException {
        SecurityUtils.responseFailCredential(response, HttpStatus.FORBIDDEN, accessDeniedException.getLocalizedMessage());

    }
}
