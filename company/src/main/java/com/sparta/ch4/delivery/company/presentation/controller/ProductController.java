package com.sparta.ch4.delivery.company.presentation.controller;


import com.sparta.ch4.delivery.company.application.service.ProductService;
import com.sparta.ch4.delivery.company.domain.type.ProductSearchType;
import com.sparta.ch4.delivery.company.presentation.request.ProductCreateRequest;
import com.sparta.ch4.delivery.company.presentation.request.ProductQuantityUpdateRequest;
import com.sparta.ch4.delivery.company.presentation.request.ProductUpdateRequest;
import com.sparta.ch4.delivery.company.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.company.presentation.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    // Header 에 들어온 userId 로 createdBy 설정
    @PostMapping
    public CommonResponse<ProductResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest request,
            @RequestHeader("X-UserId") String userId
    ) {
        return CommonResponse.success(
                ProductResponse.from(productService.createProduct(request.toDto(userId)))
        );
    }

    @GetMapping
    public CommonResponse<Page<ProductResponse>> getProducts(
            @RequestParam(required = false, name = "companyId") UUID companyId,
            @RequestParam(required = false, name = "hubId") UUID hubId,
            @RequestParam(required = false, name = "searchType") ProductSearchType searchType,
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return CommonResponse.success(
                productService.getAllProducts(companyId, hubId, searchType, searchValue, pageable).map(ProductResponse::from)
        );
    }

    @GetMapping("/{productId}")
    public CommonResponse<ProductResponse> getProduct(
            @PathVariable(name = "productId") UUID productId
    ) {
        return CommonResponse.success(
                ProductResponse.from(productService.getProductById(productId))
        );
    }

    // Header 에 들어온 userId 로 updatedBy 설정
    @PutMapping("/{productId}")
    public CommonResponse<ProductResponse> updateProduct(
            @PathVariable(name = "productId") UUID productId,
            @Valid @RequestBody ProductUpdateRequest request,
            @RequestHeader("X-UserId") String userId
    ) {
        return CommonResponse.success(
                ProductResponse.from(productService.updateProduct(productId, request.toDto(userId)))
        );
    }

    // Header 에 들어온 userId 로 deletedBy 설정
    @DeleteMapping("/{productId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable(name = "productId") UUID productId,
            @RequestHeader("X-UserId") String userId
    ) {
        productService.deleteProduct(productId, userId);
    }

    @PutMapping("/api/products/{productId}/quantity")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateQuantity(
            @PathVariable(name = "productId") UUID productId,
            @RequestBody @Valid ProductQuantityUpdateRequest request
    ){
      productService.updateProductQuantity(productId, request.quantity(), request.upAndDown());
    }
}
