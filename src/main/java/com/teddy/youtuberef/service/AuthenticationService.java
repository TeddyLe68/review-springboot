package com.teddy.youtuberef.service;

import com.teddy.youtuberef.service.dto.request.LoginRequest;
import com.teddy.youtuberef.service.dto.response.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);
}
