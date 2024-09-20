package com.sparta.ch4.delivery.order.infrastructure.client.response;


import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        CompanyResponse company,
        UUID hubId,
        String name,
        Integer quantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
