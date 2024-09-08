package com.sparta.ch4.delivery.company.presentation.request;


import com.sparta.ch4.delivery.company.application.dto.ProductDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductCreateRequest(
        @NotNull(message = "업체 ID 는 비어있을 수 없습니다.")
        UUID companyId,
        @NotNull(message = "허브 ID 는 비어있을 수 없습니다.")
        UUID hubId,
        @NotBlank(message = "상품 이름은 비어있을 수 없습니다.")
        String name,
        @Min(value = 0, message = "상품 수량은 0 이상이어야 합니다.")
        @NotNull(message = "상품 수량은 비어있을 수 없습니다.")
        Integer quantity
) {

    public ProductDto toDto(String userId) {
        return ProductDto.builder()
                .companyId(companyId)
                .hubId(hubId)
                .name(name)
                .quantity(quantity)
                .createdBy(userId)
                .build();
    }

}
