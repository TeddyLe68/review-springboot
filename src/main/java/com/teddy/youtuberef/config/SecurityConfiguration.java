package com.teddy.youtuberef.config;

import com.teddy.youtuberef.config.filter.AuthenticationFilter;
import com.teddy.youtuberef.config.properties.SecurityProperties;
import com.teddy.youtuberef.security.SecurityProblemSupport;
import com.teddy.youtuberef.security.jwt.JwtConfigurer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    final AuthenticationFilter authenticationFilter;
    final SecurityProblemSupport problemSupport;
    final SecurityProperties securityProperties;
    final JwtConfigurer jwtConfigurer;

    public static final List<String> PUBLIC_APIS = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register"
    );

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(PUBLIC_APIS.toArray(String[]::new)).permitAll()
                                        .anyRequest()
                                        .authenticated()
                )
                .rememberMe(
                        httpSecurityRememberMeConfigurer ->
                                httpSecurityRememberMeConfigurer.rememberMeParameter("remember-me")
                                .tokenValiditySeconds((int) securityProperties.getRememberMeExpiration())
                                .userDetailsService(userDetailsService)
                )
                .exceptionHandling(
                        httpSecurityExceptionHandlingConfigurer ->
                                httpSecurityExceptionHandlingConfigurer
                                        .accessDeniedHandler(problemSupport)
                                        .authenticationEntryPoint(problemSupport)
                )
                .headers(httpSecurityHeadersConfigurer ->
                        httpSecurityHeadersConfigurer
                                .referrerPolicy(
                                referrerPolicyConfig ->
                                        referrerPolicyConfig.policy(
                                                ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN
                                        )
                        )
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                                .httpStrictTransportSecurity(HeadersConfigurer.HstsConfig::disable)
                )
                .apply(jwtConfigurer);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Collections.singletonList("Content-Disposition"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
