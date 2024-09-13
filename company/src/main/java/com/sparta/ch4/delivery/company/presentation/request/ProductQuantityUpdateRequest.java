package com.sparta.ch4.delivery.company.presentation.request;

import com.sparta.ch4.delivery.company.domain.type.ProductQuantity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ProductQuantityUpdateRequest(
        Integer quantity,
        @Schema(description = "수량 추가/차감 (Enum)", enumAsRef = true)
        ProductQuantity upAndDown
) {

    public static ProductQuantityUpdateRequest from(Integer quantity, ProductQuantity upAndDown){
        return ProductQuantityUpdateRequest.builder()
                .quantity(quantity)
                .upAndDown(upAndDown)
                .build();
    }
}
