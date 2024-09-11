package com.sparta.ch4.delivery.company.infrastructure.client.response;

import com.sparta.ch4.delivery.company.domain.type.UserRole;

import java.util.UUID;

public record UserResponse(
        Long id,
        UUID hubId,
        UUID companyId,
        String username,
        String email,
        UserRole role,
        UUID slackId
){}