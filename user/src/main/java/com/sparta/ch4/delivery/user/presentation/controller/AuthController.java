package com.sparta.ch4.delivery.user.presentation.controller;

import com.sparta.ch4.delivery.user.application.service.AuthService;
import com.sparta.ch4.delivery.user.presentation.request.RegisterRequest;
import com.sparta.ch4.delivery.user.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.user.presentation.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public CommonResponse<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return CommonResponse.success(authService.register(request.toDto()));
    }

    // TODO: 로그인 시 토큰 발급
//    @PostMapping("/login")
//    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest request) {
//
//    }

    // TODO 도전 기능: 비밀번호 초기화
}
