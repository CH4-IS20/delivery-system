package com.sparta.ch4.delivery.gateway.response;

public record ErrorResponse(
       int status,
       String message
) {}