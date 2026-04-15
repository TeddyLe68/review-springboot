package com.teddy.youtuberef.service.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Username không được để trống") String username,
        @NotBlank(message = "Password không được để trống") String password,
        Boolean rememberMe)
{
}
