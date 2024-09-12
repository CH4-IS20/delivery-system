package com.sparta.ch4.delivery.order.application.dto;

import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import lombok.Builder;

@Builder
public record DeliveryStatusUpdateDto(
        DeliveryStatus status,
        String updatedBy
) {
}
