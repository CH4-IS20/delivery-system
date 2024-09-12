package com.sparta.ch4.delivery.order.presentation.controller;


import com.sparta.ch4.delivery.order.application.service.DeliveryHistoryService;
import com.sparta.ch4.delivery.order.presentation.request.DeliveryHistoryStatusUpdateRequest;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.order.presentation.response.DeliveryHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/delivery-histories")
public class DeliveryHistoryController {
    private final DeliveryHistoryService deliveryHistoryService;

    @GetMapping("/{deliveryHistoryId}")
    public CommonResponse<DeliveryHistoryResponse> getDelivery(@PathVariable UUID deliveryHistoryId) {
        return CommonResponse.success(DeliveryHistoryResponse.from(
                deliveryHistoryService.getDelivery(deliveryHistoryId))
        );
    }

    @PutMapping("/{deliveryHistoryId}")
    public CommonResponse<DeliveryHistoryResponse> updateDeliveryHistoryForRealEstimate(
            @PathVariable UUID deliveryHistoryId,
            @RequestBody DeliveryHistoryStatusUpdateRequest request,
            @RequestHeader(name = "X-UserId") String userId
    ) {
        return CommonResponse.success(
                DeliveryHistoryResponse.from(
                        deliveryHistoryService.updateDeliveryHistoryForRealEstimate(deliveryHistoryId, request.toDto(userId))
                )
        );
    }

    //Delivery Id 에 의존적
    @GetMapping("/delivery/{deliveryId}")
    public CommonResponse<List<DeliveryHistoryResponse>> getDeliveryHistoriesForDelivery(@PathVariable UUID deliveryId) {
        return CommonResponse.success(
                deliveryHistoryService.getDeliveryHistoriesForDelivery(deliveryId).stream()
                        .map(DeliveryHistoryResponse::from)
                        .toList()
        );
    }

    //Delivery Id 에 의존적
    @DeleteMapping("/delivery/{deliveryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeliveryHistoriesForDelivery(
            @PathVariable UUID deliveryId,
            @RequestHeader(name = "X-UserId") String userId
    ) {
        deliveryHistoryService.deleteAllDeliveryHistory(deliveryId, userId);
    }


}
