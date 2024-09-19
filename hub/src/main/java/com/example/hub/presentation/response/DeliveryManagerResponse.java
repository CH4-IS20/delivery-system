package com.example.hub.presentation.response;

import com.example.hub.domain.model.DeliveryManager;
import com.example.hub.domain.type.DeliveryManagerType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record DeliveryManagerResponse(
        Long userId,
        String name,
        UUID hubId,
        String slackId,
        DeliveryManagerType type
) {

    public static DeliveryManagerResponse from(DeliveryManager manager) {
        return DeliveryManagerResponse.builder()
                .userId(manager.getId())
                .name(manager.getName())
                .hubId(manager.getHub() != null ? manager.getHub().getId() : null)
                .slackId(manager.getSlackId())
                .type(manager.getType())
                .build();
    }
}
