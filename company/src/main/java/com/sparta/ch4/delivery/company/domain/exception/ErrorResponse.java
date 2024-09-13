package com.sparta.ch4.delivery.company.domain.exception;

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