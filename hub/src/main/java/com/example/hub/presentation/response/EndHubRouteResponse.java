package com.example.hub.presentation.response;

import com.example.hub.domain.model.HubRoute;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record EndHubRouteResponse(
        UUID id,
        UUID endHubId,
        double distance,            // 현재 허브로부터 거리
        double durationTime     // 현재 허브로부터 이동 시간
) {

    public static EndHubRouteResponse from(HubRoute hubRoute) {
        return EndHubRouteResponse.builder()
                .id(hubRoute.getId())
                .endHubId(hubRoute.getEndHubId())
                .distance(hubRoute.getDistance())
                .durationTime(hubRoute.getDurationTime())
                .build();
    }
}
