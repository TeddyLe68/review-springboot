package com.teddy.youtuberef.web.rest;

import com.teddy.youtuberef.service.dto.request.LoginRequest;
import com.teddy.youtuberef.service.dto.response.LoginResponse;
import com.teddy.youtuberef.service.dto.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public interface AuthController {
    @PostMapping("/login")
    public Response<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest);
}
