package com.sparta.ch4.delivery.company.presentation.controller;


import com.sparta.ch4.delivery.company.application.service.ProductService;
import com.sparta.ch4.delivery.company.domain.type.ProductSearchType;
import com.sparta.ch4.delivery.company.presentation.request.ProductCreateRequest;
import com.sparta.ch4.delivery.company.presentation.request.ProductQuantityUpdateRequest;
import com.sparta.ch4.delivery.company.presentation.request.ProductUpdateRequest;
import com.sparta.ch4.delivery.company.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.company.presentation.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "Product API", description = "상품 CRUD")
public class ProductController {
    private final ProductService productService;

    // Header 에 들어온 userId 로 createdBy 설정
    @PostMapping
    @Operation(summary = "상품 생성", description = "상품 생성 API")
    public CommonResponse<ProductResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest request,
            @RequestHeader("X-UserId") String userId
    ) {
        return CommonResponse.success(
                ProductResponse.from(productService.createProduct(request.toDto(userId), userId))
        );
    }

    @GetMapping
    @Operation(summary = "상품 전체 조회", description = "상품 전체 조회 API")
    public CommonResponse<Page<ProductResponse>> getProducts(
            @Parameter(name = "companyId", description = "상품의 관리 업체 ID")
            @RequestParam(required = false, name = "companyId") UUID companyId,
            @Parameter(name = "hubId", description = "상품의 관리 허브 ID")
            @RequestParam(required = false, name = "hubId") UUID hubId,
            @Parameter(name = "searchType", description = "검색 조건 (Enum) 설정", schema = @Schema(implementation = ProductSearchType.class))
            @RequestParam(required = false, name = "searchType") ProductSearchType searchType,
            @Parameter(name = "searchValue", description = "검색 조건 기준으로 한 검색어")
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @ParameterObject @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return CommonResponse.success(
                productService.getAllProducts(companyId, hubId, searchType, searchValue, pageable).map(ProductResponse::from)
        );
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 단건 조회", description = "상품 단건 조회 API")
    public CommonResponse<ProductResponse> getProduct(
            @PathVariable(name = "productId") UUID productId
    ) {
        return CommonResponse.success(
                ProductResponse.from(productService.getProductById(productId))
        );
    }

    // Header 에 들어온 userId 로 updatedBy 설정
    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "상품 정보 수정 API, [관리 업체, 허브, 상품이름, 재고]")
    public CommonResponse<ProductResponse> updateProduct(
            @PathVariable(name = "productId") UUID productId,
            @Valid @RequestBody ProductUpdateRequest request,
            @RequestHeader("X-UserId") String userId
    ) {
        return CommonResponse.success(
                ProductResponse.from(productService.updateProduct(productId, request.toDto(userId), userId))
        );
    }

    // Header 에 들어온 userId 로 deletedBy 설정
    @DeleteMapping("/{productId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "상품 삭제", description = "상품 삭제 API,")
    public void delete(
            @PathVariable(name = "productId") UUID productId,
            @RequestHeader("X-UserId") String userId
    ) {
        productService.deleteProduct(productId, userId);
    }

    @PutMapping("/{productId}/quantity")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "[for Order API] 상품 수량 추가/차감", description = "주문한 상품 재고 수정 API")
    public void updateQuantity(
            @PathVariable(name = "productId") UUID productId,
            @RequestBody @Valid ProductQuantityUpdateRequest request
    ){
      productService.updateProductQuantity(productId, request.quantity(), request.upAndDown());
    }
}
