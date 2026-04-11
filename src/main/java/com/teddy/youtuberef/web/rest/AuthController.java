package com.teddy.youtuberef.web.rest;

import com.teddy.youtuberef.service.dto.AccountDto;
import com.teddy.youtuberef.service.dto.request.LoginRequest;
import com.teddy.youtuberef.service.dto.request.RegisterAccountRequest;
import com.teddy.youtuberef.service.dto.response.LoginResponse;
import com.teddy.youtuberef.service.dto.response.Response;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public interface AuthController {
    @PostMapping("/login")
    public Response<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest);

    @PutMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response<AccountDto> register(@Valid RegisterAccountRequest registerAccountRequest);
}
