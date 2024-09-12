package com.sparta.ch4.delivery.order.presentation.request;

import com.sparta.ch4.delivery.order.application.dto.DeliveryStatusUpdateDto;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;

public record DeliveryStatusUpdateRequest(
        DeliveryStatus status
) {

    public DeliveryStatusUpdateDto toDto(String updatedBy){
        return DeliveryStatusUpdateDto.builder()
                .status(status)
                .updatedBy(updatedBy)
                .build();
    }
}
