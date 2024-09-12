package com.sparta.ch4.delivery.order.application.dto;

import com.sparta.ch4.delivery.order.domain.model.Delivery;
import com.sparta.ch4.delivery.order.domain.model.Order;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DeliveryDto(
        UUID id, // 배송 ID
        DeliveryStatus status, // 배송 상태
        UUID startHub,      // 배송 시작 허브
        UUID endHub,        // 배송 목적지 허브
        String deliveryAddress,     // 배송지(수령업체주소)
        String recipient,  // 수령자(DeliveryManager - 업체소속)
        String recipientSlack,     // 수령자 slack id
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy,
        Boolean isDeleted       // 배송 삭제 여부
) {

    public static DeliveryDto from(Delivery delivery) {
        return DeliveryDto.builder()
                .id(delivery.getId())
                .status(delivery.getStatus())
                .startHub(delivery.getStartHub())
                .endHub(delivery.getEndHub())
                .deliveryAddress(delivery.getDeliveryAddress())
                .recipient(delivery.getRecipient())
                .recipientSlack(delivery.getRecipientSlack())
                .createdAt(delivery.getCreatedAt())
                .createdBy(delivery.getCreatedBy())
                .updatedAt(delivery.getUpdatedAt())
                .updatedBy(delivery.getUpdatedBy())
                .deletedAt(delivery.getDeletedAt())
                .deletedBy(delivery.getDeletedBy())
                .isDeleted(delivery.getIsDeleted())
                .build();
    }

    public Delivery toEntity(Order order, String userIdForCreatedBy) {
        Delivery delivery = Delivery.builder()
                .id(id)
                .order(order)
                .status(status)
                .startHub(startHub)
                .endHub(endHub)
                .deliveryAddress(deliveryAddress)
                .recipient(recipient)
                .recipientSlack(recipientSlack)
                .build();
        delivery.setCreatedBy(userIdForCreatedBy);
        return delivery;
    }
}
