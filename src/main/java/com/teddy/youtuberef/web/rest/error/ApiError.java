package com.teddy.youtuberef.web.rest.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    /*
     * {
     *  "timestamp": "2024-06-01T12:00:00",
     *  "code": 400,
     *  "message": "Bad Request",
     *  "apiSubErrors": [
     *      "Field 'name' is required",
     *      "Field 'email' must be a valid email address"
     *  ]
     * }
     */
    @JsonFormat(pattern = "dd/mm/yyy hh:mm:ss")
    LocalDateTime timestamp = LocalDateTime.now();
    int code;
    String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<ApiSubError> apiSubErrors = new ArrayList<>();

    public ApiError(int code){
        this.code = code;
    }

    public ApiError(String message){
        this.message = message;
    }

    public ApiError(int code, String message){
        this.code = code;
        this.message = message;
    }
}
