package com.sparta.ch4.delivery.order.presentation.response;

import com.sparta.ch4.delivery.order.application.dto.DeliveryDto;
import com.sparta.ch4.delivery.order.application.dto.OrderDto;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DeliveryWithOrderResponse(
        UUID id, // 배송 ID
        OrderResponse order,
        DeliveryStatus status, // 배송 상태
        UUID startHub,      // 배송 시작 허브
        UUID endHub,        // 배송 목적지 허브
        String deliveryAddress,     // 배송지(수령업체주소)
        String recipient,  // 수령자(DeliveryManager - 업체소속)
        String recipientSlack,     // 수령자 slack id
        Boolean isDeleted
) {
    public static DeliveryWithOrderResponse from(DeliveryDto dto) {
        return DeliveryWithOrderResponse.builder()
                .id(dto.id())
                .order(OrderResponse.from(
                        OrderDto.builder()
                                .id(dto.orderId())
                                .userId(dto.orderUserId())
                                .supplierId(dto.orderSupplierId())
                                .receiverId(dto.orderReceiverId())
                                .productId(dto.orderProductId())
                                .quantity(dto.orderQuantity())
                                .status(dto.orderStatus())
                                .isDeleted(dto.orderIsDeleted())
                                .build()
                ))
                .status(dto.status())
                .startHub(dto.startHub())
                .endHub(dto.endHub())
                .deliveryAddress(dto.deliveryAddress())
                .recipient(dto.recipient())
                .recipientSlack(dto.recipientSlack())
                .isDeleted(dto.isDeleted())
                .build();
    }
}
