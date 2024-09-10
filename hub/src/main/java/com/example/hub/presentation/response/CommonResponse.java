package com.example.hub.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommonResponse<T> {

    private int status;
    private String message;
    private T data;

    public CommonResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }


    // 엔티티 생성 / 조회 / 수정 성공 반환
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(200, "success", data);
    }


    // 삭제 성공 반환
    public static <T> CommonResponse<T> success(String message) {
        return new CommonResponse<>(204, message);
    }

    // 에러 반환
    public static <T> CommonResponse<T> error(T data) {
        return new CommonResponse<>(204, "ERROR",data);
    }
}