package com.sparta.ch4.delivery.order.application.dto;

import com.sparta.ch4.delivery.order.domain.model.Order;
import com.sparta.ch4.delivery.order.domain.type.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderCreateDto(
        Long userId,
        UUID supplierId,
        UUID receiverId,
        UUID productId,
        Integer quantity,
        LocalDateTime orderDate,
        OrderStatus status,
        String receiptAddress,
        String recipientName,
        String recipientSlack,
        String createdBy
) {

    public Order toEntity(){
        Order order = Order.builder()
                .userId(userId)
                .supplierId(supplierId)
                .receiverId(receiverId)
                .productId(productId)
                .quantity(quantity)
                .orderDate(orderDate)
                .status(status)
                .build();
        order.setCreatedBy(createdBy);
        return order;
    }
}
