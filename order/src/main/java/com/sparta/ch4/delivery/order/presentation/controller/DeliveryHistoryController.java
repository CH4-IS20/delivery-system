package com.sparta.ch4.delivery.order.presentation.controller;


import com.sparta.ch4.delivery.order.application.service.DeliveryHistoryService;
import com.sparta.ch4.delivery.order.presentation.request.DeliveryHistoryStatusUpdateRequest;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.order.presentation.response.DeliveryHistoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/delivery-histories")
@Tag(name = "DeliveryHistory API", description = "배송 경로 Read, Update, Delete")
public class DeliveryHistoryController {
    private final DeliveryHistoryService deliveryHistoryService;

    @GetMapping("/{deliveryHistoryId}")
    @Operation(summary = "배송 기록 단건 조회", description = "배송 기록 단건 조회 API")
    public CommonResponse<DeliveryHistoryResponse> getDeliveryHistory(@PathVariable UUID deliveryHistoryId) {
        return CommonResponse.success(DeliveryHistoryResponse.from(
                deliveryHistoryService.getDelivery(deliveryHistoryId))
        );
    }

    @PutMapping("/{deliveryHistoryId}")
    @Operation(summary = "배송 기록 현황 수정", description = "배송 기록 status 수정 API, [실거리, 실소요시간, 상태] 수정")
    public CommonResponse<DeliveryHistoryResponse> updateDeliveryHistoryForRealEstimate(
            @PathVariable UUID deliveryHistoryId,
            @RequestBody @Valid DeliveryHistoryStatusUpdateRequest request,
            @RequestHeader(name = "X-UserId") String userId
    ) {
        return CommonResponse.success(
                DeliveryHistoryResponse.from(
                        deliveryHistoryService.updateDeliveryHistoryForRealEstimate(deliveryHistoryId, request.toDto(userId))
                )
        );
    }

    @GetMapping("/deliveries/{deliveryId}")
    public CommonResponse<List<DeliveryHistoryResponse>> getDeliveryHistoriesForDelivery(
            @PathVariable(name = "deliveryId") UUID deliveryId
    ) {
        return CommonResponse.success(
                deliveryHistoryService.getDeliveryHistoriesForDelivery(deliveryId).stream()
                        .map(DeliveryHistoryResponse::from)
                        .toList()
        );
    }

    @DeleteMapping("/deliveries/{deliveryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeliveryHistoriesForDelivery(
            @PathVariable UUID deliveryId,
            @RequestHeader(name = "X-UserId") String userId
    ) {
        deliveryHistoryService.deleteAllDeliveryHistory(deliveryId, userId);
    }
}
