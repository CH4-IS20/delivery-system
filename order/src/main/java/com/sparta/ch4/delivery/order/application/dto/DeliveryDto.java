package com.sparta.ch4.delivery.order.application.dto;

import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import com.sparta.ch4.delivery.order.domain.type.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DeliveryDto(
        UUID id, // 배송 ID
        Long orderUserId, // 주문자 ID
        UUID orderSupplierId,  // 요청(공급) 업체
        UUID orderReceiverId,     // 수령 업체 ID
        UUID orderProductId,  // 상품 ID
        Integer orderQuantity, // 주문 상품 수량
        LocalDateTime order_date,  // 주문 일자 및 시간
        OrderStatus orderStatus,  // 주문 상태
        DeliveryStatus status, // 배송 상태
        UUID startHub,      // 배송 시작 허브
        UUID endHub,        // 배송 목적지 허브
        String deliveryAddress,     // 배송지(수령업체주소)
        String recipient,  // 수령자(DeliveryManager - 업체소속)
        UUID recipientSlack     // 수령자 slackID
) {
}
