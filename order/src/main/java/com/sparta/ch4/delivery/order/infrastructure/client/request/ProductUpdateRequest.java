package com.sparta.ch4.delivery.order.infrastructure.client.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductUpdateRequest(
        @NotNull(message = "업체 ID 는 비어있을 수 없습니다.")
        UUID companyId,
        @NotNull(message = "허브 ID 는 비어있을 수 없습니다.")
        UUID hubId,
        @NotBlank(message = "상품 이름은 비어있을 수 없습니다.")
        String name,
        @Min(value = 0, message = "상품 수량은 0 이상이어야 합니다.")
        @NotNull(message = "상품 수량은 비어있을 수 없습니다.")
        Integer quantity
) {}
