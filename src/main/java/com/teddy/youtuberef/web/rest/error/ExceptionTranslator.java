package com.teddy.youtuberef.web.rest.error;

import com.teddy.youtuberef.common.AppConstant;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionTranslator implements ResponseErrorHandler {

    private ResponseEntity<ErrorResponse> badRequest(ErrorResponse result){
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> internalServerError(ErrorResponse result){
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> notFound(ErrorResponse result){
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErrorResponse> forbidden(ErrorResponse result){
        return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
    }
    private ResponseEntity<ErrorResponse> unauthorized(ErrorResponse result) {
        return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex){
        Map<String, Object> map = new HashMap<>();
        map.put("service", ex.getMessage());
        return internalServerError(new ErrorResponse(AppConstant.SERVICE_ERROR.getCode(),AppConstant.SERVICE_ERROR.getMessage(),map));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){
        final Map<String, Object> errorResults = new HashMap<>();
        for(FieldError error : ex.getBindingResult().getFieldErrors()){
            errorResults.put(error.getField(), error.getDefaultMessage());
        }
        final ErrorResponse response = new ErrorResponse(
                AppConstant.BAD_REQUEST.getCode(),
                "Validation Failed",
                errorResults
        );
        return badRequest(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("service", ex.getMessage());
        return forbidden(
                new ErrorResponse(AppConstant.FORBIDDEN.getCode(), AppConstant.SERVICE_ERROR.getMessage(), map)
        );
    }
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseEntity<ErrorResponse> handleAccessDeniedException(AuthenticationException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("service", ex.getMessage());
        return unauthorized(
                new ErrorResponse(AppConstant.UNAUTHORIZED.getCode(), AppConstant.UNAUTHORIZED.getMessage(), map)
        );
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return false;
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        ResponseErrorHandler.super.handleError(url, method, response);
    }
}
