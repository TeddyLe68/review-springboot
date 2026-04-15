package com.teddy.youtuberef.service.impl;

import com.teddy.youtuberef.common.utils.jwtUtil;
import com.teddy.youtuberef.config.properties.SecurityProperties;
import com.teddy.youtuberef.entity.AccountEntity;
import com.teddy.youtuberef.exception.AuthenticationException;
import com.teddy.youtuberef.intergration.minio.MinioChannel;
import com.teddy.youtuberef.repository.AccountRepository;
import com.teddy.youtuberef.repository.RoleRepository;
import com.teddy.youtuberef.security.jwt.TokenProvider;
import com.teddy.youtuberef.service.AuthenticationService;
import com.teddy.youtuberef.service.dto.AccountDto;
import com.teddy.youtuberef.service.dto.request.LoginRequest;
import com.teddy.youtuberef.service.dto.request.RegisterAccountRequest;
import com.teddy.youtuberef.service.dto.response.LoginResponse;
import com.teddy.youtuberef.service.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    // Other
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // Properties
    private final SecurityProperties securityProperties;

    // Mapper
    private final AccountMapper accountMapper;

    // Chanel
    private final MinioChannel minioChannel;

    // Repository
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;


    @Override
    public LoginResponse login(LoginRequest request) {
        final var authenticationToken = new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        );
        // Xác thực người dùng bằng cách sử dụng AuthenticationManager để kiểm tra thông tin đăng nhập
        final var authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Tạo token JWT cho người dùng đã xác thực và trả về trong LoginResponse với thời gian sống của token dựa trên tùy chọn "rememberMe" trong request
        return new LoginResponse(tokenProvider.createToken(authentication, Optional.ofNullable(request.rememberMe()).orElse(false)));
    }

    @Override
    public AccountDto register(RegisterAccountRequest registerAccountRequest) {
        final var account = new AccountEntity();
        account.setUsername(registerAccountRequest.getUsername());
        account.setPasswordHash(passwordEncoder.encode(registerAccountRequest.getPassword()));
        account.setAvatar(minioChannel.upload(registerAccountRequest.getAvatar()));
        account.setUuid(UUID.randomUUID().toString());
        account.setRoles(roleRepository.findAll());
        return accountMapper.toDto(
                accountRepository.save(account)
        );
    }
}
