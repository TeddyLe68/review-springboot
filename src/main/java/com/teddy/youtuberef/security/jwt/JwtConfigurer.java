package com.teddy.youtuberef.security.jwt;

import com.teddy.youtuberef.config.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthenticationFilter authenticationFilter;
    @Override
    public void init(HttpSecurity http) {

    }

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
