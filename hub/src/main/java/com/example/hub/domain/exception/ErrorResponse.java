package com.example.hub.domain.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private Map<String, String> validation;


    // 에러 반환
    public static ErrorResponse error(ErrorCode errorCode) {
        return ErrorResponse.builder().status(errorCode.getStatus())
                .message(errorCode.getMessage()).build();
    }

    // Request 에러 필드에 대한 처리
    public void addValidation(String field, String errorMessage) {
        if (validation == null) {
            validation = new HashMap<>();
        }
        this.validation.put(field, errorMessage);
    }
}