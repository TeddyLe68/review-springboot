package com.teddy.youtuberef.service.impl;

import com.teddy.youtuberef.common.utils.jwtUtil;
import com.teddy.youtuberef.config.SecurityProperties;
import com.teddy.youtuberef.entity.AccountEntity;
import com.teddy.youtuberef.exception.AuthenticationException;
import com.teddy.youtuberef.repository.AccountRepository;
import com.teddy.youtuberef.service.AuthenticationService;
import com.teddy.youtuberef.service.dto.request.LoginRequest;
import com.teddy.youtuberef.service.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;
    @Override
    public LoginResponse login(LoginRequest request) {
        final var account = accountRepository.findByUsername(request.username())
                .orElseThrow(()-> new AuthenticationException("Invalid username or password"));

        if(ObjectUtils.isEmpty(request.password()) || !passwordEncoder.matches(request.password(),account.getPasswordHash()) ) {
            throw new AuthenticationException("Invalid username or password");
        }

        final var token = jwtUtil.generateToken(account, securityProperties.getJwtSecret(), securityProperties.getJwtExpiration());

        return new LoginResponse(token);
    }
}
