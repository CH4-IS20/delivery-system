package com.sparta.ch4.delivery.company.presentation.request;

import com.sparta.ch4.delivery.company.application.dto.CompanyDto;
import com.sparta.ch4.delivery.company.domain.type.CompanyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CompanyCreateRequest(
        @NotNull(message = "허브 ID 는 비어있을 수 없습니다.")
        UUID hubId,
        @NotBlank(message = "업체 이름은 비어있을 수 없습니다.")
        String name,
        @Schema(description = "업체타입 (Enum)", enumAsRef = true)
        @NotNull(message = "업체 타입은 비어있을 수 없습니다.")
        CompanyType type,
        @NotBlank(message = "업체 주소는 비어있을 수 없습니다.")
        String address
) {

    public CompanyDto toDto(String userId) {
        return CompanyDto.builder()
                .hubId(hubId)
                .name(name)
                .type(type)
                .address(address)
                .createdBy(userId)
                .build();
    }
}
