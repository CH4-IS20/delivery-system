package com.sparta.ch4.delivery.order.presentation.response;

import com.sparta.ch4.delivery.order.application.dto.OrderDto;
import com.sparta.ch4.delivery.order.domain.type.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderResponse(
        UUID id,  // 주문 ID
        Long userId, // 주문자 ID
        UUID supplierId,  // 요청(공급) 업체
        UUID receiverId,     // 수령 업체 ID
        UUID productId,  // 상품 ID
        Integer quantity, // 주문 상품 수량
        LocalDateTime orderDate,  // 주문 일자 및 시간
        OrderStatus status,  // 주문 상태
        Boolean isDeleted
) {

    public static OrderResponse from(OrderDto dto) {
        return OrderResponse.builder()
                .id(dto.id())
                .userId(dto.userId())
                .supplierId(dto.supplierId())
                .receiverId(dto.receiverId())
                .productId(dto.productId())
                .quantity(dto.quantity())
                .orderDate(dto.orderDate())
                .status(dto.status())
                .isDeleted(dto.isDeleted())
                .build();
    }
}
