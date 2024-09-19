package com.sparta.ch4.delivery.order.infrastructure.client.response;


import java.util.UUID;

public record HubRouteForOrderResponse(
        String storeName, // 업체 이름
        UUID hubRouteId,  // 허브 경로 엔티티 ID
        UUID deliveryManagerId,  //배송담당자
        UUID startHubId, // 시작 허브
        UUID destHubId, //  목적지 허브
        Float estimatedDistance,   // 예상 거리
        Integer estimatedDuration   //예상 소요 시간
) {
}
