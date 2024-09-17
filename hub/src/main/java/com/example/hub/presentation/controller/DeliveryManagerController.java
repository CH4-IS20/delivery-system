package com.example.hub.presentation.controller;

import com.example.hub.application.service.DeliveryManagerService;
import com.example.hub.presentation.request.DeliveryManagerCreateRequest;
import com.example.hub.presentation.response.CommonResponse;
import com.example.hub.presentation.response.DeliveryManagerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/delivery-managers")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    // 배송담당자 생성     (마스터만 가능)
    @PostMapping
    public CommonResponse<DeliveryManagerResponse> createDeliveryManager(
            @RequestBody DeliveryManagerCreateRequest request
    ){
        return CommonResponse.success(deliveryManagerService.createDeliveryManager(request));
    }

    // 배송담당자 세부 확인 (마스터, 배송담당자 본인, 허브 관리자)
    @GetMapping("/{deliveryManagerId}")
    public CommonResponse<DeliveryManagerResponse> readDeliveryManager(
            @PathVariable UUID deliveryManagerId,
            @RequestHeader(value = "X-User-Id", required = true) String userId,
            @RequestHeader(value = "X-Role", required = true) String role) {
        return CommonResponse.success(deliveryManagerService.readDeliveryManager(deliveryManagerId,userId,role));
    }

    // 배송 담당자 전부 출력 (허브별 검색 가능)       (마스터만 가능)
    @GetMapping
    public CommonResponse<Page<DeliveryManagerResponse>> readDeliveryManagers(
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @PageableDefault(size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
        return CommonResponse.success(deliveryManagerService.searchDeliveryManager(searchValue,pageable));
    }
    
    // 배송 담당자 수정 (마스터, 허브 관리자만 가능)
    @PutMapping("/{deliveryManagerId}")
    public CommonResponse<DeliveryManagerResponse> updateDeliveryManager(
            @RequestBody DeliveryManagerCreateRequest request,
            @PathVariable UUID deliveryManagerId,
            @RequestHeader(value = "X-User-Id", required = true) String userId,
            @RequestHeader(value = "X-Role", required = true) String role
    ){

        return CommonResponse.success(deliveryManagerService.updateDeliveryManager(request,deliveryManagerId,userId,role));
    }

    // 배송 담당자 삭제 (마스터, 허브 관리자만 가능)
    @DeleteMapping("/{deliveryManagerId}")
    public CommonResponse<String> deleteDeliveryManager(
            @PathVariable UUID deliveryManagerId,
            @RequestHeader(value = "X-User-Id", required = true) String userId,
            @RequestHeader(value = "X-Role", required = true) String role
    ){
        deliveryManagerService.deleteDeliveryManager(deliveryManagerId, userId, role);
        return CommonResponse.success(deliveryManagerId + "에 해당하는 배송 담당자 정보가 삭제되었습니다.");
    }
}


