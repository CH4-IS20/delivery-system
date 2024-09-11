package com.sparta.ch4.delivery.order.infrastructure.client.response;

import com.sparta.ch4.delivery.order.domain.type.CompanyType;

import java.util.UUID;

public record CompanyWithUserForOrderResponse(
        UUID id,
        UUID hubId,
        String name,
        CompanyType type,
        String address,
        String recipient,  // 주문 수령자
        UUID recipientSlack  // 주문 수령자 slackID
){}