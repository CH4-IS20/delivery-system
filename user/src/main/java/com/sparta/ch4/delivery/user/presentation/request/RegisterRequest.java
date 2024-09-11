package com.sparta.ch4.delivery.user.presentation.request;

import com.sparta.ch4.delivery.user.application.dto.RegisterDto;
import com.sparta.ch4.delivery.user.domain.type.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record RegisterRequest(
        @NotBlank(message = "사용자 이름은 필수입니다.")
        @Size(min = 4, max = 10, message = "사용자 이름은 4자에서 10자 사이여야 합니다.")
        @Pattern(regexp = "^[a-z0-9]+$", message = "사용자 이름은 소문자 알파벳과 숫자만 포함해야 합니다.")
        String username,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 15, message = "비밀번호는 8자에서 15자 사이여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).+$",
                message = "비밀번호는 최소 한 개의 알파벳, 숫자, 특수 문자를 포함해야 합니다.")
        String password,

        @NotNull(message = "역할은 필수입니다.")
        UserRole role,

        @NotBlank(message = "Slack 아이디는 필수입니다.")
        String slackId,

        UUID hubId,

        UUID companyId
) {
        public RegisterDto toDto() {
                return RegisterDto.builder()
                        .hubId(hubId)
                        .companyId(companyId)
                        .slackId(slackId)
                        .username(username)
                        .password(password)
                        .email(email)
                        .role(role)
                        .build();
        }
}


