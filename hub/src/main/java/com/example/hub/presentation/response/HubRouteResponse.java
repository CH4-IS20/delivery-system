package com.example.hub.presentation.response;


import com.example.hub.domain.model.HubRoute;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Builder
public record HubRouteResponse(
        UUID id,
        UUID startHubId,
        double distance,            // 현재 허브로부터 거리
        double durationTime,     // 현재 허브로부터 이동 시간
        List<EndHubRouteResponse> endHub
){

    public static HubRouteResponse from(HubRoute hubRoute) {
        return HubRouteResponse.builder()
                .id(hubRoute.getId())
                .startHubId(hubRoute.getStartHubId())
                .distance(hubRoute.getDistance())
                .durationTime(hubRoute.getDurationTime())
                .endHub(hubRoute.getEndHub() != null ? hubRoute.getEndHub().stream()
                        .map(h -> EndHubRouteResponse.from(h) )
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}

