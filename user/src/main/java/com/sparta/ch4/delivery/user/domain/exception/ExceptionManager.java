package com.sparta.ch4.delivery.user.domain.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse<?>> applicationExceptionHandler(ApplicationException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.error(e.getErrorCode()));
    }
}