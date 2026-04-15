package com.teddy.youtuberef.security;

import com.teddy.youtuberef.entity.AccountEntity;
import com.teddy.youtuberef.entity.RoleEntity;
import com.teddy.youtuberef.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Authenticate a user from database
 */
@Slf4j
@Component("userDetailsService")
@RequiredArgsConstructor
public class DomainUserDetailsService implements UserDetailsService {

    final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Authenticating = {}", username);
        if (new EmailValidator().isValid(username, null)) {
            return accountRepository
                    .findByUsername(username)
                    .map(this::createSpringSecurityUser)
                    .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " was not found in the database"));
        }
        final var lowercaseLogin = username.toLowerCase(Locale.ENGLISH);
        return accountRepository
                .findByUsername(lowercaseLogin)
                .map(this::createSpringSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
    }

    private User createSpringSecurityUser(AccountEntity accountEntity) {
        final var grantedAuthorities = accountEntity
                .getRoles()
                .stream()
                .map(RoleEntity::getName)
                .map(Enum::name)
                .map((SimpleGrantedAuthority::new))
                .collect(Collectors.toList());
        return new User(accountEntity.getUsername(), accountEntity.getPasswordHash(), grantedAuthorities);
    }
}
