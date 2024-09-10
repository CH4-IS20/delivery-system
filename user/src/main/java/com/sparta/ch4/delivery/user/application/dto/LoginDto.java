package com.sparta.ch4.delivery.user.application.dto;

import lombok.Builder;

@Builder
public record LoginDto (
        String username,
        String password
)
{

}
