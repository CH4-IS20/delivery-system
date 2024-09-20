package com.sparta.ch4.delivery.order.presentation.controller;


import com.sparta.ch4.delivery.order.application.service.OrderService;
import com.sparta.ch4.delivery.order.domain.type.OrderSearchType;
import com.sparta.ch4.delivery.order.presentation.request.OrderCreateRequest;
import com.sparta.ch4.delivery.order.presentation.request.OrderUpdateRequest;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.order.presentation.response.OrderResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "주문 CRUD")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "주문 생성", description = "주문 생성 API")
    public CommonResponse<OrderResponse> createOrder(
            @RequestBody @Valid OrderCreateRequest request,
            @RequestHeader(name = "X-UserId") String userId
    ) {
        return CommonResponse.success(
                OrderResponse.from(orderService.createOrder(request.toDto(userId))));
    }

    @GetMapping
    @Operation(summary = "주문 전체 조회(검색)", description = "주문 전체 조회 API, 페이징 결과를 반환")
    public CommonResponse<Page<OrderResponse>> getOrders(
            @Parameter(name = "searchType", description = "검색 조건 (Enum) 설정", schema = @Schema(implementation = OrderSearchType.class))
            @RequestParam(required = false, name = "searchType") OrderSearchType searchType,
            @Parameter(name = "searchValue", description = "검색 조건 기준으로 한 검색어")
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @ParameterObject @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable
    ){
        return CommonResponse.success(
                orderService.getAllOrders(searchType, searchValue, pageable).map(OrderResponse::from)
        );
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "주문 단건 조회", description = "주문 단건 조회 API")
    public CommonResponse<OrderResponse> getOrder(@PathVariable UUID orderId){
        return CommonResponse.success(
                OrderResponse.from(orderService.getOrder(orderId))
        );
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "주문 수정", description = "주문 단건 수정 API, 주문에 포함된 상품의 수량을 수정 가능")
    public CommonResponse<OrderResponse> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody @Valid OrderUpdateRequest request,
            @RequestHeader(name = "X-UserId") String userId
    ){
        return CommonResponse.success(
                OrderResponse.from(orderService.updateOrder(orderId, request.toDto(userId)))
        );
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "주문 삭제", description = "주문 삭제 API")
    public void deleteOrder(
            @PathVariable UUID orderId,
            @RequestHeader(name = "X-UserId") String userId
    ){
        orderService.deleteOrder(orderId,userId);
    }
}
