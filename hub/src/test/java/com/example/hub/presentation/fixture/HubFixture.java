package com.example.hub.presentation.fixture;

import com.example.hub.domain.model.Hub;
import lombok.Builder;

import java.util.UUID;


@Builder
public class HubFixture {
    public static Hub get(){
        return Hub.builder()
                .id(UUID.fromString("1l"))
                .name("서울특별시센터")
                .address("서울특별시 송파구 송파대로 55")
                .latitude(37.4969143)
                .longitude(127.1121343)
                .build();
    }
}