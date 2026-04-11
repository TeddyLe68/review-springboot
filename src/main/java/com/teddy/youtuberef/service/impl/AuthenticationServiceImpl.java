package com.teddy.youtuberef.service.impl;

import com.teddy.youtuberef.common.utils.jwtUtil;
import com.teddy.youtuberef.config.SecurityProperties;
import com.teddy.youtuberef.entity.AccountEntity;
import com.teddy.youtuberef.exception.AuthenticationException;
import com.teddy.youtuberef.intergration.minio.MinioChannel;
import com.teddy.youtuberef.repository.AccountRepository;
import com.teddy.youtuberef.repository.RoleRepository;
import com.teddy.youtuberef.service.AuthenticationService;
import com.teddy.youtuberef.service.dto.AccountDto;
import com.teddy.youtuberef.service.dto.request.LoginRequest;
import com.teddy.youtuberef.service.dto.request.RegisterAccountRequest;
import com.teddy.youtuberef.service.dto.response.LoginResponse;
import com.teddy.youtuberef.service.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
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
