package com.sparta.ch4.delivery.order.infrastructure.client.request;

import com.sparta.ch4.delivery.order.domain.type.ProductQuantity;
import lombok.Builder;

@Builder
public record ProductQuantityUpdateRequest(
        Integer quantity,
        ProductQuantity upAndDown
) {

    public static ProductQuantityUpdateRequest from(Integer quantity, ProductQuantity upAndDown){
        return ProductQuantityUpdateRequest.builder()
                .quantity(quantity)
                .upAndDown(upAndDown)
                .build();
    }
}
