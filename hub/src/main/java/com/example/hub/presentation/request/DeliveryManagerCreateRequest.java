package com.example.hub.presentation.request;

import com.example.hub.domain.model.DeliveryManager;
import com.example.hub.domain.model.Hub;
import com.example.hub.domain.type.DeliveryManagerType;

import java.util.UUID;

public record DeliveryManagerCreateRequest (
        UUID userId,
        String name,
        UUID hubId,
        String slackId,
        DeliveryManagerType type
){

    public static DeliveryManager to(DeliveryManagerCreateRequest request, Hub hub) {
        return DeliveryManager.builder()
                .id(request.userId)
                .name(request.name())
                .hub(hub)
                .slackId(request.slackId())
                .type(request.type)
                .status(false)
                .build();
    }
}