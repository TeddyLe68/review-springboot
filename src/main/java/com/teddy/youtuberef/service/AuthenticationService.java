package com.teddy.youtuberef.service;

import com.teddy.youtuberef.service.dto.AccountDto;
import com.teddy.youtuberef.service.dto.request.LoginRequest;
import com.teddy.youtuberef.service.dto.request.RegisterAccountRequest;
import com.teddy.youtuberef.service.dto.response.LoginResponse;

public interface AuthenticationService {
    AccountDto register(RegisterAccountRequest registerAccountRequest) ;

    LoginResponse login(LoginRequest request);
}
