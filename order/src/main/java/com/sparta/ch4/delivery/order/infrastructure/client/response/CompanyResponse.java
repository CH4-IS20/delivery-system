package com.sparta.ch4.delivery.order.infrastructure.client.response;

import com.sparta.ch4.delivery.order.domain.type.CompanyType;

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