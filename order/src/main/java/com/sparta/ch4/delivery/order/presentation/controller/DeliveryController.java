package com.sparta.ch4.delivery.order.presentation.controller;


import com.sparta.ch4.delivery.order.application.service.DeliveryService;
import com.sparta.ch4.delivery.order.domain.type.DeliverySearchType;
import com.sparta.ch4.delivery.order.domain.type.OrderSearchType;
import com.sparta.ch4.delivery.order.presentation.request.DeliveryStatusUpdateRequest;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.order.presentation.response.DeliveryResponse;
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
@RequestMapping("/api/deliveries")
@Tag(name = "Delivery API", description = "배송 Read, Update, Delete")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    @Operation(summary = "배송 전체 조회", description = "주문 전체 조회 API")
    public CommonResponse<Page<DeliveryResponse>> getDeliveries(
            @Parameter(name = "startHubId", description = "배송이 시작되는 허브 ID")
            @RequestParam(required = false, name = "startHubId") UUID startHubId,
            @Parameter(name = "endHubId", description = "배송이 끝나는 허브 ID")
            @RequestParam(required = false, name = "endHubId") UUID endHubId,
            @Parameter(name = "searchType", description = "검색 조건 (Enum) 설정", schema = @Schema(implementation = DeliverySearchType.class))
            @RequestParam(required = false, name = "searchType") DeliverySearchType searchType,
            @Parameter(name = "searchValue", description = "검색 조건 기준으로 한 검색어")
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @ParameterObject @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return CommonResponse.success(
                deliveryService.getAllDelivery(startHubId,endHubId,searchType,searchValue,pageable)
                        .map(DeliveryResponse::from)
        );
    }
    @GetMapping("/{deliveryId}")
    @Operation(summary = "배송 단건 조회", description = "주문 전체 조회 API")
    public CommonResponse<DeliveryResponse> getDelivery(
            @PathVariable(name = "deliveryId") UUID deliveryId
    ){
        return CommonResponse.success(DeliveryResponse.from(deliveryService.getDelivery(deliveryId)));
    }

    @PutMapping("/{deliveryId}")
    @Operation(summary = "배송 수정", description = "배송 수정 API, 배송 현황 업데이트")
    public CommonResponse<DeliveryResponse> updateDeliveryStatus(
            @PathVariable(name = "deliveryId") UUID deliveryId,
            @RequestBody @Valid DeliveryStatusUpdateRequest request,
            @RequestHeader(name = "X-UserId") String userId
    ){
        return CommonResponse.success(
                DeliveryResponse.from(deliveryService.updateDelivery(deliveryId, request.toDto(userId)))
        );
    }


    @DeleteMapping("/{deliveryId}")
    @Operation(summary = "배송 취소(삭제)", description = "배송 삭제 API")
    public void cancelDelivery(
            @PathVariable(name = "deliveryId") UUID deliveryId,
            @RequestHeader(name = "X-UserId") String userId
    ){
        deliveryService.deleteDelivery(deliveryId, userId);
    }


}
