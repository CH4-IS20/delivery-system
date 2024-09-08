package com.sparta.ch4.delivery.company.presentation.controller;


import com.sparta.ch4.delivery.company.application.service.ProductService;
import com.sparta.ch4.delivery.company.presentation.request.CompanyCreateRequest;
import com.sparta.ch4.delivery.company.presentation.request.ProductCreateRequest;
import com.sparta.ch4.delivery.company.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.company.presentation.response.CompanyResponse;
import com.sparta.ch4.delivery.company.presentation.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    // Header 에 들어온 userId 로 createdBy 설정
    @PostMapping
    public CommonResponse<ProductResponse> createCompany(
            @RequestBody ProductCreateRequest request,
            @RequestHeader("X-UserId") String userId
    ) {
        return CommonResponse.success(
                ProductResponse.from(productService.createProduct(request.toDto(userId)))
        );
    }

    @GetMapping("/{companyId}")
    public CommonResponse<ProductResponse> getCompany(
            @PathVariable(name = "companyId") UUID companyId
    ) {
        return CommonResponse.success(
                ProductResponse.from(productService.getProductById(companyId))
        );
    }
}
