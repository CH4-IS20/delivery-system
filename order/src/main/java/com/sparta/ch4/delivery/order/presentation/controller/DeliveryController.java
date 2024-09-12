package com.sparta.ch4.delivery.order.presentation.controller;


import com.sparta.ch4.delivery.order.application.service.DeliveryService;
import com.sparta.ch4.delivery.order.domain.type.DeliverySearchType;
import com.sparta.ch4.delivery.order.presentation.request.DeliveryStatusUpdateRequest;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.order.presentation.response.DeliveryResponse;
import com.sparta.ch4.delivery.order.presentation.response.DeliveryWithOrderResponse;
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
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public CommonResponse<Page<DeliveryResponse>> getDeliveries(
            @RequestParam(required = false, name = "startHubId") UUID startHubId,
            @RequestParam(required = false, name = "endHubId") UUID endHubId,
            @RequestParam(required = false, name = "searchType") DeliverySearchType searchType,
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return CommonResponse.success(
                deliveryService.getAllDelivery(startHubId,endHubId,searchType,searchValue,pageable)
                        .map(DeliveryResponse::from)
        );
    }
    @GetMapping("/{deliveryId}")
    public CommonResponse<DeliveryWithOrderResponse> getDelivery(
            @PathVariable(name = "deliveryId") UUID deliveryId
    ){
        return CommonResponse.success(DeliveryWithOrderResponse.from(deliveryService.getDelivery(deliveryId)));
    }

    @PutMapping("/{deliveryId}/status")
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
    public void cancelDelivery(
            @PathVariable(name = "deliveryId") UUID deliveryId,
            @RequestHeader(name = "X-UserId") String userId
    ){
        deliveryService.deleteDelivery(deliveryId, userId);
    }


}
