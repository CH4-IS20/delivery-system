package com.sparta.ch4.delivery.order.infrastructure.client;


import com.sparta.ch4.delivery.order.infrastructure.client.request.ProductQuantityUpdateRequest;
import com.sparta.ch4.delivery.order.infrastructure.client.request.ProductUpdateRequest;
import com.sparta.ch4.delivery.order.infrastructure.client.response.ProductResponse;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "product")
public interface ProductClient {

    @GetMapping("/api/products/{productId}")
    CommonResponse<ProductResponse> getProductById(@PathVariable UUID productId);

    @PutMapping("/api/products/{productId}")
    CommonResponse<ProductResponse> updateProduct(
            @PathVariable UUID productId, @RequestBody @Valid ProductUpdateRequest request
    );


    /**
     *
     * @param productId
     * @Body: quantity(Integer), upAndDown(ProductQuantity)
     * @Description: request 요청으로 들어온 quantity 에 대해 더하거나 빼는 API
     */
    @PutMapping("/api/products/{productId}/quantity")
    void updateQuantity(
            @PathVariable(name = "productId") UUID productId,
            @RequestBody @Valid ProductQuantityUpdateRequest request
    );
}
