package com.sparta.ch4.delivery.order.application.dto;

import com.sparta.ch4.delivery.order.domain.model.DeliveryHistory;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DeliveryHistoryDto(
        UUID id,
        Long deliveryManagerId,
        UUID hubRouteId,
        Integer sequence,
        Float estimatedDistance,
        Integer estimatedDuration,
        Float actualDistance,
        Integer actualDuration,
        DeliveryStatus status,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy,
        Boolean isDeleted
) {

    public static DeliveryHistoryDto from(DeliveryHistory entity) {
        return DeliveryHistoryDto.builder()
                .id(entity.getId())
                .deliveryManagerId(entity.getDeliveryManagerId())
                .hubRouteId(entity.getHubRouteId())
                .sequence(entity.getSequence())
                .estimatedDistance(entity.getEstimatedDistance())
                .estimatedDuration(entity.getEstimatedDuration())
                .actualDistance(entity.getActualDistance())
                .actualDuration(entity.getActualDuration())
                .status(entity.getStatus())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .isDeleted(entity.getIsDeleted())
                .build();
    }
}
