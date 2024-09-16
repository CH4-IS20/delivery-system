package com.sparta.ch4.delivery.order.presentation.request;

import com.sparta.ch4.delivery.order.application.dto.DeliveryStatusUpdateDto;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public record DeliveryStatusUpdateRequest(
        @NotNull(message = "배달 상태는 비어있을 수 없습니다.")
        DeliveryStatus status
) {

    public DeliveryStatusUpdateDto toDto(String updatedBy){
        return DeliveryStatusUpdateDto.builder()
                .status(status)
                .updatedBy(updatedBy)
                .build();
    }
}
