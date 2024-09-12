package com.sparta.ch4.delivery.order.presentation.response;

import com.sparta.ch4.delivery.order.application.dto.DeliveryDto;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DeliveryResponse(
        UUID id, // 배송 ID
        DeliveryStatus status, // 배송 상태
        UUID orderId,    // orderId
        UUID startHub,      // 배송 시작 허브
        UUID endHub,        // 배송 목적지 허브
        String deliveryAddress,     // 배송지(수령업체주소)
        String recipient,  // 수령자(DeliveryManager - 업체소속)
        String recipientSlack,     // 수령자 slack id
        LocalDateTime createdAt,
        Boolean isDeleted       // 삭제(취소)여부
) {

    public static DeliveryResponse from(DeliveryDto dto) {
        return DeliveryResponse.builder()
                .id(dto.id())
                .status(dto.status())
                .orderId(dto.orderId())
                .startHub(dto.startHub())
                .endHub(dto.endHub())
                .deliveryAddress(dto.deliveryAddress())
                .recipient(dto.recipient())
                .recipientSlack(dto.recipientSlack())
                .createdAt(dto.createdAt())
                .isDeleted(dto.isDeleted())
                .build();
    }
}
