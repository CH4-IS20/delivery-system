package com.sparta.ch4.delivery.user.presentation.request;

import com.sparta.ch4.delivery.user.application.dto.LoginDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청 데이터")
public record LoginRequest(

        @NotBlank(message = "사용자 이름은 필수입니다.")
        @Size(min = 4, max = 10, message = "사용자 이름은 4자에서 10자 사이여야 합니다.")
        @Pattern(regexp = "^[a-z0-9]+$", message = "사용자 이름은 소문자 알파벳과 숫자만 포함해야 합니다.")
        @Schema(description = "사용자 이름, 소문자 알파벳과 숫자만 포함 가능", example = "user123")
        String username,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 15, message = "비밀번호는 8자에서 15자 사이여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).+$",
                message = "비밀번호는 최소 한 개의 알파벳, 숫자, 특수 문자를 포함해야 합니다.")
        @Schema(description = "비밀번호, 최소 하나의 알파벳, 숫자, 특수 문자를 포함", example = "Passw0rd!")
        String password
) {
        public LoginDto toDto() {
                return LoginDto.builder()
                        .username(username)
                        .password(password)
                        .build();
        }
}