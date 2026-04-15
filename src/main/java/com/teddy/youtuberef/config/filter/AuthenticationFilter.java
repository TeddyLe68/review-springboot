package com.teddy.youtuberef.config.filter;

import com.teddy.youtuberef.common.utils.jwtUtil;
import com.teddy.youtuberef.config.properties.SecurityProperties;
import com.teddy.youtuberef.entity.AccountEntity;
import com.teddy.youtuberef.entity.RoleEntity;
import com.teddy.youtuberef.repository.AccountRepository;
import com.teddy.youtuberef.security.jwt.TokenProvider;
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
import java.util.List;

import static com.teddy.youtuberef.config.SecurityConfiguration.PUBLIC_APIS;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter extends OncePerRequestFilter {
    static String AUTHORIZATION_HEADER = "Authorization";
    static String AUTHORIZATION_TOKEN =  "access_token";
    TokenProvider tokenProvider;


    SecurityProperties securityProperties;
    AccountRepository accountRepository;

    static List<String> API_PUBLIC = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register"
    );

    /**
     *
     * This method is used to determine whether the filter should be applied to a given request.
     * @param request
     * @return
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        final var path = request.getRequestURI();
        return PUBLIC_APIS.contains(path);
    }


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
        String jwt = resolveToken(request);
        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        String jwt = request.getParameter(AUTHORIZATION_TOKEN);
        if (StringUtils.hasText(jwt)) {
            return jwt;
        }
        return null;
    }
}
