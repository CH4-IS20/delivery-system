package com.sparta.ch4.delivery.order.infrastructure.client;


import com.sparta.ch4.delivery.order.infrastructure.client.request.ProductUpdateRequest;
import com.sparta.ch4.delivery.order.infrastructure.client.response.ProductResponse;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "product")
public interface ProductClient {

    @GetMapping("/api/products/{productId}")
    CommonResponse<ProductResponse> getProductById(@PathVariable UUID productId);

    @PutMapping("/api/products/{productId}")
    CommonResponse<ProductResponse> updateProduct(
            @PathVariable UUID productId, @RequestBody @Valid ProductUpdateRequest request
    );
}
