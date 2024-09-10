package com.sparta.ch4.delivery.user.application.dto;

import com.sparta.ch4.delivery.user.domain.type.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserPageDto(
        Long id,
        UUID hubId,
        UUID companyId,
        String username,
        String email,
        UserRole role,
        LocalDateTime createdAt
) {

}
