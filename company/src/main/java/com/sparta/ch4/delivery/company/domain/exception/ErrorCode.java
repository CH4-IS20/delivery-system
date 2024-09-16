package com.sparta.ch4.delivery.company.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //COMPANY
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 업체를 찾지 못하였습니다."),
    DUPLICATED_COMPANY(HttpStatus.CONFLICT, "이미 존재하는 업체 이름입니다."),

    //PRODUCT
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾지 못하였습니다."),

    //Authorization
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),


    //Client Error
    // 통신이 됐는데 null 로 넘어오면 API 에러
    CLIENT_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "API 요청 에러"),
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브를 찾지 못하였습니다.");


    private HttpStatus status;
    private String message;

}