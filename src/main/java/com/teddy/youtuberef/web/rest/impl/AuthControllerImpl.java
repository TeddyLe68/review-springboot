package com.teddy.youtuberef.web.rest.impl;

import com.teddy.youtuberef.service.AuthenticationService;
import com.teddy.youtuberef.service.dto.request.LoginRequest;
import com.teddy.youtuberef.service.dto.response.LoginResponse;
import com.teddy.youtuberef.service.dto.response.Response;
import com.teddy.youtuberef.web.rest.AuthController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthenticationService authenticationService;

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) {
        return Response.ok(authenticationService.login(loginRequest));
    }
}
