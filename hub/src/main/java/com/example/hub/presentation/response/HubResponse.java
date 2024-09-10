package com.example.hub.presentation.response;

import com.example.hub.application.dto.HubDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public record HubResponse(
        UUID id,
        String name,
        String address,
        double latitude,
        double longitude
){

    public static HubResponse fromDto(HubDto dto) {
        return HubResponse.builder()
                .id(dto.id())
                .name(dto.name())
                .address(dto.address())
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .build();
    }
}
