package com.teddy.youtuberef.web.rest.impl;

import com.teddy.youtuberef.service.AuthenticationService;
import com.teddy.youtuberef.service.dto.AccountDto;
import com.teddy.youtuberef.service.dto.request.LoginRequest;
import com.teddy.youtuberef.service.dto.request.RegisterAccountRequest;
import com.teddy.youtuberef.service.dto.response.LoginResponse;
import com.teddy.youtuberef.service.dto.response.Response;
import com.teddy.youtuberef.web.rest.AuthController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<Response<LoginResponse>> login(LoginRequest loginRequest) {
        final var loginResponse = authenticationService.login(loginRequest);
        final var httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.token());
        return new ResponseEntity<>(Response.ok(loginResponse), httpHeaders, HttpStatus.OK);
    }

    @Override
    public Response<AccountDto> register(RegisterAccountRequest registerAccountRequest) {
        return Response.created(authenticationService.register(registerAccountRequest));
    }
}
