package com.example.hub.application.dto;

import com.example.hub.domain.model.Hub;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record HubDto (
        UUID id,
        String name,
        String address,
        double latitude,
        double longitude
){

    public static HubDto fromEntity(Hub hub) {
        return HubDto.builder()
                .id(hub.getId())
                .name(hub.getName())
                .address(hub.getAddress())
                .latitude(hub.getLatitude())
                .longitude(hub.getLongitude())
                .build();
    }
}
