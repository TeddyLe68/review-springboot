package com.teddy.youtuberef.config.filter;

import com.teddy.youtuberef.common.utils.jwtUtil;
import com.teddy.youtuberef.config.SecurityProperties;
import com.teddy.youtuberef.entity.AccountEntity;
import com.teddy.youtuberef.entity.RoleEntity;
import com.teddy.youtuberef.repository.AccountRepository;
import com.teddy.youtuberef.web.rest.error.MessageCode;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter extends OncePerRequestFilter {
    SecurityProperties securityProperties;
    AccountRepository accountRepository;

    static List<String> API_PUBLIC = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register"
    );

    /**
     * This method is called for every incoming HTTP request.
     * It allows you to perform authentication checks before the request is processed further.
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (API_PUBLIC.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
        }else {
            final var authentication = getAuthentication(request, response);
            if (!ObjectUtils.isEmpty(authentication)) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                responseFailCredential(response, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    /**
     * This method is responsible for extracting the JWT token from the Authorization header of the HTTP request,
        * validating the token, and returning an Authentication object if the token is valid.
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public Authentication getAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        final var authorization = httpServletRequest.getHeader("Authorization");
        UsernamePasswordAuthenticationToken authentication = null;

        // Check if the Authorization header is present and starts with "Bearer "
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            final var token = authorization.substring(7);
            // Validate the JWT token using the jwtUtil class and the secret key from securityProperties
            try {
                jwtUtil.validateJwtToken(token,securityProperties.getJwtSecret() );
            }catch (Exception e){
                responseFailCredential(httpServletResponse,HttpStatus.UNAUTHORIZED);
                return null;
            }
            // After validating the token, extract the user UUID from the token and retrieve the corresponding account from the database
            final var uuid = jwtUtil.getUserUuidFromJwtToken(token, securityProperties.getJwtSecret());
            final var account = this.accountRepository.findByUuid(uuid)
                    .orElseThrow(EntityNotFoundException::new);

            // If the account is found, create a UsernamePasswordAuthenticationToken
            // with the account details and authorities, and set it in the SecurityContext
            authentication = new UsernamePasswordAuthenticationToken(account,null,buildAuthorities(account));

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        }
        return authentication;
    }

    /**
     * This method is responsible for sending a failure response when authentication fails.
     * @param httpServletResponse
     * @param status
     * @throws IOException
     *
     * Response structure:
     * {
     *   "code": "401",
     *   "message": "Unauthorized"
     * }
     */
    private void responseFailCredential(HttpServletResponse httpServletResponse, HttpStatus status) throws IOException {
        httpServletResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(status.value());
        new ObjectMapper().writeValue(
                httpServletResponse.getOutputStream(),
                new MessageCode(
                        String.valueOf(status.value()),
                        status.getReasonPhrase()));
        httpServletResponse.flushBuffer();
    }

    /**
     *
     * @param account
     * @return
     *
     * This method is responsible for building a list of GrantedAuthority
     * objects based on the roles associated with the given AccountEntity.
     */
    private List<? extends GrantedAuthority> buildAuthorities(AccountEntity account) {
        return account.getRoles().stream()
                .map(RoleEntity::getName)
                .map(Enum::name)
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
