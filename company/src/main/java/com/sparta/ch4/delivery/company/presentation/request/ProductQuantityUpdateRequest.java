package com.sparta.ch4.delivery.company.presentation.request;

import com.sparta.ch4.delivery.company.domain.type.ProductQuantity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProductQuantityUpdateRequest(
        @Min(value = 0)
        Integer quantity,
        @Schema(description = "수량 추가/차감 (Enum)", enumAsRef = true)
        @NotNull(message = "추가/차감 중 선택해야 합니다.")
        ProductQuantity upAndDown
) {

    public static ProductQuantityUpdateRequest from(Integer quantity, ProductQuantity upAndDown) {
        return ProductQuantityUpdateRequest.builder()
                .quantity(quantity)
                .upAndDown(upAndDown)
                .build();
    }
}
