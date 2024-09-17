package com.example.hub.application.dto;

import com.example.hub.domain.model.Hub;
import com.example.hub.domain.model.HubRoute;
import lombok.Builder;


import java.util.UUID;

@Builder
public record EndHubRouteDto(
        UUID endHubId,
        String endHubName,
        double distance,            // 현재 허브로부터 거리
        double durationTime     // 현재 허브로부터 이동 시간
){

    public static EndHubRouteDto from(Hub hub, double dis,double dur){
        return EndHubRouteDto.builder()
                .endHubId(hub.getId())
                .endHubName(hub.getName())
                .distance(dis)
                .durationTime(dur)
                .build();
    }

    public static HubRoute to(EndHubRouteDto dto,HubRoute hubRoute){
        return HubRoute.builder()
                .startHubId(null)
                .parent(hubRoute)
                .endHubId(dto.endHubId)
                .endHubName(dto.endHubName)
                .distance(dto.distance)
                .durationTime(dto.durationTime)
                .build();
    }

}
