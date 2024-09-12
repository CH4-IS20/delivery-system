package com.example.hub.presentation.fixture;

import com.example.hub.presentation.request.HubCreateRequest;

public class HubCreateRequestFixture {
    public static HubCreateRequest get(){
        return HubCreateRequest.builder()
                .name("서울특별시센터")
                .build();
    }
}
