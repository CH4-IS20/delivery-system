package com.sparta.ch4.delivery.company.infrastructure.client.response;


import java.util.UUID;

public record HubResponse(
        UUID id,
        String name,
        String address,
        double latitude,
        double longitude
){}
