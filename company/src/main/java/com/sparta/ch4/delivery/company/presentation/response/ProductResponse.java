package com.sparta.ch4.delivery.company.presentation.response;

import com.sparta.ch4.delivery.company.application.dto.CompanyDto;
import com.sparta.ch4.delivery.company.application.dto.ProductDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ProductResponse(
        UUID id,
        CompanyResponse company,
        UUID hubId,
        String name,
        Integer quantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ProductResponse from(ProductDto dto) {
        return ProductResponse.builder()
                .id(dto.id())
                .company(CompanyResponse.from(CompanyDto.builder()
                        .id(dto.companyId())
                        .hubId(dto.companyHubId())
                        .type(dto.companyType())
                        .name(dto.companyName())
                        .address(dto.companyAddress())
                        .createdAt(dto.companyCreatedAt())
                        .build()))
                .hubId(dto.hubId())
                .name(dto.name())
                .quantity(dto.quantity())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .build();
    }
}
