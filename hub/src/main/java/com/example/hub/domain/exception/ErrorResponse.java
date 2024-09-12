package com.example.hub.domain.exception;

import com.example.hub.presentation.response.CommonResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse<T> {
    private HttpStatus status;
    private String message;

    // 에러 반환
    public static <T> ErrorResponse<T> error(ErrorCode errorCode) {
        return new ErrorResponse<>(errorCode.getStatus(), errorCode.getMessage());
    }
}