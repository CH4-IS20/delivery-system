package com.example.hub.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾지 못하였습니다."),
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브를 찾지 못하였습니다."),
    
    DUPLICATED_HUBNAME(HttpStatus.CONFLICT, "이미 해당 허브가 존재합니다."),

    NOTFOUND_VALUE(HttpStatus.NOT_FOUND, "입력한 데이터가 없습니다"),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");

    private HttpStatus status;
    private String message;

}