package com.sparta.ch4.delivery.user.application.dto;

import com.sparta.ch4.delivery.user.domain.model.User;
import com.sparta.ch4.delivery.user.domain.type.UserRole;
import java.util.UUID;
import lombok.Builder;

@Builder
public record RegisterDto(
        UUID hubId,
        UUID companyId,
        String slackId,
        String username,
        String email,
        String password,
        UserRole role,
        Boolean isDeleted
) {

    public User toEntity(String encodedPassword) {
        return User.builder()
                .hubId(hubId)
                .companyId(companyId)
                .slackId(slackId)
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role(role)
                .build();
    }


}
