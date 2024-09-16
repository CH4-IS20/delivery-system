package com.sparta.ch4.delivery.user.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ID 에 해당하는 사용자가 존재하지 않습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자 이름입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 아이디에 해당하는 사용자가 없습니다."),
    SLACK_SEND_MESSAGE_FAIL(HttpStatus.BAD_REQUEST, "Slack 메시지 전송 중 문제가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

}