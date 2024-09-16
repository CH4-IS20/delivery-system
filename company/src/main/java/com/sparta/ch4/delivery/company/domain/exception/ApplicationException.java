package com.sparta.ch4.delivery.company.domain.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public ApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    @Override
    public String toString() {
        return "CompanyException{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                '}';
    }
}