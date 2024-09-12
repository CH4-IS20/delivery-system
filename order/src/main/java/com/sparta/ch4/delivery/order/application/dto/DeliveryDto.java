package com.sparta.ch4.delivery.order.application.dto;

import com.sparta.ch4.delivery.order.domain.model.Delivery;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import com.sparta.ch4.delivery.order.domain.type.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DeliveryDto(
        UUID id, // 배송 ID
        UUID orderId,   // 주문 아이디
        Long orderUserId, // 주문자 ID
        UUID orderSupplierId,  // 요청(공급) 업체
        UUID orderReceiverId,     // 수령 업체 ID
        UUID orderProductId,  // 상품 ID
        Integer orderQuantity, // 주문 상품 수량
        LocalDateTime orderDate,  // 주문 일자 및 시간
        OrderStatus orderStatus,  // 주문 상태
        Boolean orderIsDeleted,     // 주문 삭제 여부
        DeliveryStatus status, // 배송 상태
        UUID startHub,      // 배송 시작 허브
        UUID endHub,        // 배송 목적지 허브
        String deliveryAddress,     // 배송지(수령업체주소)
        String recipient,  // 수령자(DeliveryManager - 업체소속)
        String recipientSlack,     // 수령자 slack id
        LocalDateTime createdAt,
        Boolean isDeleted       // 배송 삭제 여부
) {

    public static DeliveryDto from(Delivery entity) {
        return DeliveryDto.builder()
                .id(entity.getId())
                .orderId(entity.getOrder().getId())
                .orderUserId(entity.getOrder().getUserId())
                .orderSupplierId(entity.getOrder().getSupplierId())
                .orderProductId(entity.getOrder().getProductId())
                .orderQuantity(entity.getOrder().getQuantity())
                .orderDate(entity.getOrder().getOrderDate())
                .orderStatus(entity.getOrder().getStatus())
                .orderIsDeleted(entity.getOrder().getIsDeleted())
                .status(entity.getStatus())
                .startHub(entity.getStartHub())
                .endHub(entity.getEndHub())
                .deliveryAddress(entity.getDeliveryAddress())
                .recipient(entity.getRecipient())
                .recipientSlack(entity.getRecipientSlack())
                .createdAt(entity.getCreatedAt())
                .isDeleted(entity.getIsDeleted())
                .build();
    }
}
