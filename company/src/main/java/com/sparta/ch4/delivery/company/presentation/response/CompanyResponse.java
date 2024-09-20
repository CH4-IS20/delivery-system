package com.sparta.ch4.delivery.company.presentation.response;

import com.sparta.ch4.delivery.company.application.dto.CompanyDto;
import com.sparta.ch4.delivery.company.domain.type.CompanyType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CompanyResponse(
        UUID id,
        UUID hubId,
        String name,
        CompanyType type,
        String address,
        LocalDateTime createdAt
) {

    public static CompanyResponse from(CompanyDto dto) {
        return CompanyResponse.builder()
                .id(dto.id())
                .hubId(dto.hubId())
                .name(dto.name())
                .type(dto.type())
                .address(dto.address())
                .createdAt(dto.createdAt())
                .build();
    }
}
