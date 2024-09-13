package com.sparta.ch4.delivery.order.presentation.response;

import com.sparta.ch4.delivery.order.application.dto.DeliveryHistoryDto;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record DeliveryHistoryResponse(
        UUID id,
        UUID deliveryManagerId,
        UUID hubRouteId,
        Integer sequence,
        Float estimatedDistance,
        Integer estimatedDuration,
        Float actualDistance,
        Integer actualDuration,
        DeliveryStatus status,
        Boolean isDeleted
) {

    public static DeliveryHistoryResponse from(DeliveryHistoryDto dto) {
        return DeliveryHistoryResponse.builder()
                .id(dto.id())
                .deliveryManagerId(dto.deliveryManagerId())
                .hubRouteId(dto.hubRouteId())
                .sequence(dto.sequence())
                .estimatedDistance(dto.estimatedDistance())
                .estimatedDuration(dto.estimatedDuration())
                .actualDistance(dto.actualDistance())
                .actualDuration(dto.actualDuration())
                .status(dto.status())
                .isDeleted(dto.isDeleted())
                .build();

    }
}
