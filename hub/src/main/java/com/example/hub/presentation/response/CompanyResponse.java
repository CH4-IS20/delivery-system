package com.example.hub.presentation.response;

import com.example.hub.domain.type.CompanyType;

import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyResponse(
        UUID id,
        UUID hubId,
        String name,
        CompanyType type,
        String address,
        LocalDateTime createdAt
) {
}