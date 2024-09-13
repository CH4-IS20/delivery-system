package com.sparta.ch4.delivery.order.presentation.controller;


import com.sparta.ch4.delivery.order.application.service.OrderService;
import com.sparta.ch4.delivery.order.domain.type.OrderSearchType;
import com.sparta.ch4.delivery.order.presentation.request.OrderCreateRequest;
import com.sparta.ch4.delivery.order.presentation.request.OrderUpdateRequest;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.order.presentation.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public CommonResponse<OrderResponse> createOrder(
            @RequestBody @Valid OrderCreateRequest request,
            @RequestHeader(name = "X-UserId") String userId
    ) {
        return CommonResponse.success(
                OrderResponse.from(orderService.createOrder(request.toDto(userId))));
    }

    @GetMapping
    public CommonResponse<Page<OrderResponse>> getOrders(
            @RequestParam(required = false, name = "searchType") OrderSearchType searchType,
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable
    ){
        return CommonResponse.success(
                orderService.getAllOrders(searchType, searchValue, pageable).map(OrderResponse::from)
        );
    }

    @GetMapping("/{orderId}")
    public CommonResponse<OrderResponse> getOrder(@PathVariable UUID orderId){
        return CommonResponse.success(
                OrderResponse.from(orderService.getOrder(orderId))
        );
    }

    @PutMapping("/{orderId}")
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
    public void deleteOrder(
            @PathVariable UUID orderId,
            @RequestHeader(name = "X-UserId") String userId
    ){
        orderService.deleteOrder(orderId,userId);
    }
}
