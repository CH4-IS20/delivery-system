package com.sparta.ch4.delivery.order.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾지 못하였습니다."),

    // Hub
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브를 찾지 못하였습니다."),
    HUBROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브경로를 찾지 못하였습니다."),
    DELIVERYMANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배송담당자를 찾지 못하였습니다."),
    DELIVERYMANAGER_DUPLICATED(HttpStatus.CONFLICT, "해당 배송담당자는 이미 다른 허브에 배정되었습니다"),
    DUPLICATED_HUBNAME(HttpStatus.CONFLICT, "이미 해당 허브가 존재합니다."),
    HUB_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "허브 API 요청에 문제가 있습니다."),
    //Company
    COMPANY_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "업체 API 요청에 문제가 있습니다."),

    //Product
    PRODUCT_INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "주문의 상품 ID와 일치하지 않습니다."),
    //Order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND,"주문 정보를 찾을 수 없습니다."),

    //Delivery
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 정보를 찾을 수 없습니다."),
    //Delivery History
    DELIVERY_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "ID에 해당하는 배송 기록 정보를 찾을 수 없습니다."),
    DELIVERY_HISTORY_DELETE_ERROR(HttpStatus.BAD_REQUEST, "삭제되지 않은 배송의 기록은 삭제할 수 없습니다."),

    // I/O
    NOTFOUND_VALUE(HttpStatus.NOT_FOUND, "입력한 데이터가 없습니다"),

    // Role Auth
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");


    private HttpStatus status;
    private String message;

}