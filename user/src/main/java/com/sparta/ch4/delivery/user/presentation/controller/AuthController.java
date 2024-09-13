package com.sparta.ch4.delivery.user.presentation.controller;

import com.sparta.ch4.delivery.user.application.service.AuthService;
import com.sparta.ch4.delivery.user.presentation.request.LoginRequest;
import com.sparta.ch4.delivery.user.presentation.request.RegisterRequest;
import com.sparta.ch4.delivery.user.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.user.presentation.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "사용자 인증 및 회원가입을 처리하는 API입니다.")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입이 성공적으로 완료되었습니다.")
    @PostMapping("/register")
    public CommonResponse<UserResponse> register(
            @Parameter(description = "회원가입 요청 데이터") @Valid @RequestBody RegisterRequest request) {
        return CommonResponse.success(authService.register(request.toDto()));
    }

    @Operation(summary = "로그인", description = "사용자 로그인 처리를 합니다.")
    @ApiResponse(responseCode = "200", description = "로그인이 성공적으로 완료되었습니다.")
    @ApiResponse(responseCode = "404", description = "사용자 아이디에 해당하는 사용자가 없습니다.")
    @ApiResponse(responseCode = "401", description = "비밀번호가 일치하지 않습니다.")
    @PostMapping("/login")
    public CommonResponse<UserResponse> login(
            @Parameter(description = "로그인 요청 데이터") @RequestBody LoginRequest request,
            @Parameter(description = "응답 헤더") HttpServletResponse response) {
        return CommonResponse.success(authService.login(response, request.toDto()));
    }

    // TODO 도전 기능: 비밀번호 초기화
}

