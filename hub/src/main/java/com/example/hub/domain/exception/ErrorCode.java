package com.example.hub.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾지 못하였습니다."),
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브를 찾지 못하였습니다."),
    DELIVERYMANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배송담당자를 찾지 못하였습니다."),
    DELIVERYMANAGER_DUPLICATED(HttpStatus.CONFLICT, "해당 배송담당자는 이미 다른 허브에 배정되었습니다"),

    DUPLICATED_HUBNAME(HttpStatus.CONFLICT, "이미 해당 허브가 존재합니다."),

    NOTFOUND_VALUE(HttpStatus.NOT_FOUND, "입력한 데이터가 없습니다"),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    DELIVERY_OVERCROWDED(HttpStatus.TOO_MANY_REQUESTS, "이미 허브에 배정될 수 있는 인원이 찼습니다");

    private HttpStatus status;
    private String message;

}