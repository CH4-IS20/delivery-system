package com.sparta.ch4.delivery.order.application.dto;

import com.sparta.ch4.delivery.order.domain.model.Order;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import com.sparta.ch4.delivery.order.domain.type.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderDto(
        UUID id,
        Long userId, // 주문자 ID
        UUID supplierId,  // 요청(공급) 업체
        UUID receiverId,     // 수령 업체 ID
        UUID productId,  // 상품 ID
        Integer quantity, // 주문 상품 수량
        LocalDateTime orderDate,  // 주문 일자 및 시간
        OrderStatus status,  // 주문 상태
        DeliveryDto deliveryDto,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy,
        Boolean isDeleted
) {

    public static OrderDto from(Order entity) {
        return OrderDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .supplierId(entity.getSupplierId())
                .receiverId(entity.getReceiverId())
                .productId(entity.getProductId())
                .quantity(entity.getQuantity())
                .orderDate(entity.getOrderDate())
                .status(entity.getStatus())
                .deliveryDto(DeliveryDto.from(entity.getDelivery()))
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .isDeleted(entity.getIsDeleted())
                .build();
    }
}
