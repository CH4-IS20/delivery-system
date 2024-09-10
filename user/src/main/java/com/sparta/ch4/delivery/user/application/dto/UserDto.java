package com.sparta.ch4.delivery.user.application.dto;

import com.sparta.ch4.delivery.user.domain.model.User;
import com.sparta.ch4.delivery.user.domain.type.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        UUID hubId,
        UUID companyId,
        String username,
        String email,
        String password,
        UserRole role,
        Boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {

    public static UserDto from(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .hubId(entity.getHubId())
                .companyId(entity.getCompanyId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .role(entity.getRole())
                .password(entity.getPassword())
                .isDeleted(entity.getIsDeleted())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }

}

