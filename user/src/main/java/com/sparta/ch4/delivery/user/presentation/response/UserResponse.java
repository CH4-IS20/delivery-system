package com.sparta.ch4.delivery.user.presentation.response;

import com.sparta.ch4.delivery.user.application.dto.UserDto;
import com.sparta.ch4.delivery.user.application.dto.UserPageDto;
import com.sparta.ch4.delivery.user.domain.model.User;
import com.sparta.ch4.delivery.user.domain.type.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        UUID hubId,
        UUID companyId,
        String slackId,
        String username,
        String email,
        UserRole role,
        LocalDateTime createdAt
) {
    public static UserResponse fromUserEntity(User entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .hubId(entity.getHubId())
                .companyId(entity.getCompanyId())
                .slackId(entity.getSlackId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .role(entity.getRole())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static UserResponse fromUserDto(UserDto dto) {
        return UserResponse.builder()
                .id(dto.id())
                .hubId(dto.hubId())
                .companyId(dto.companyId())
                .slackId(dto.slackId())
                .username(dto.username())
                .email(dto.email())
                .role(dto.role())
                .createdAt(dto.createdAt())
                .build();
    }

    public static UserResponse fromUserPageDto(UserPageDto dto) {
        return UserResponse.builder()
                .id(dto.id())
                .hubId(dto.hubId())
                .companyId(dto.companyId())
                .slackId(dto.slackId())
                .username(dto.username())
                .email(dto.email())
                .role(dto.role())
                .createdAt(dto.createdAt())
                .build();
    }
}

