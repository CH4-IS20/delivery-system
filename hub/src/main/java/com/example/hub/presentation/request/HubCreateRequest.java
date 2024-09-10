package com.example.hub.presentation.request;


import com.example.hub.domain.model.Hub;
import lombok.Builder;

@Builder
public record HubCreateRequest(
        String name
) {

    public static Hub toEntity(String name, String address, double latitude, double longitude){
        return Hub.builder()
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}