package com.sparta.ch4.delivery.order.presentation.request;

import com.sparta.ch4.delivery.order.application.dto.OrderDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderUpdateRequest(
        @NotNull(message = "상품 ID 는 비어있을 수 없습니다.")
        UUID productId,  // 상품 ID

        @NotNull(message = "주문 상품 수량은 비어있을 수 없습니다.")
        @Min(value = 0)
        Integer quantity
) {

    public OrderDto toDto(String updatedBy) {
        return OrderDto.builder()
                .productId(productId)
                .quantity(quantity)
                .updatedBy(updatedBy)
                .build();
    }
}
