package com.example.hub.presentation.response;


import com.example.hub.domain.model.HubRoute;
import lombok.Builder;

import java.util.UUID;

@Builder
public record HubRouteForOrderResponse(
        String storeName,      // 출발 업체
        UUID hubRouteId,  // 허브 경로 엔티티 ID
        UUID deliveryManagerId,  //배송담당자
        UUID startHubId, // 시작 허브
        UUID destHubId, //  목적지 허브
        double estimatedDistance,   // 예상 거리
        double estimatedDuration   //예상 소요 시간
) {


    public static HubRouteForOrderResponse from(HubRoute hubRoute, UUID deliveryManagerId, UUID startHubId) {
        return HubRouteForOrderResponse.builder()
                .storeName(null)
                .hubRouteId(hubRoute.getId())
                .deliveryManagerId(deliveryManagerId)
                .startHubId(startHubId)
                .destHubId(hubRoute.getEndHubId())
                .estimatedDistance(hubRoute.getDistance())
                .estimatedDuration(hubRoute.getDurationTime())
                .build();
    }

    public static HubRouteForOrderResponse storeStartFrom(String storeName,UUID hubId, UUID deliveryManagerId, double estimatedDistance, double estimatedDuration) {
        return HubRouteForOrderResponse.builder()
                .storeName(storeName)
                .hubRouteId(null)
                .deliveryManagerId(deliveryManagerId)
                .startHubId(hubId)
                .destHubId(null)
                .estimatedDistance(estimatedDistance)
                .estimatedDuration(estimatedDuration)
                .build();
    }

    public static HubRouteForOrderResponse storeEndFrom(String storeName,UUID hubId,UUID deliveryManagerId, double estimatedDistance, double estimatedDuration) {
        return HubRouteForOrderResponse.builder()
                .storeName(storeName)
                .hubRouteId(null)
                .deliveryManagerId(deliveryManagerId)
                .startHubId(null)
                .destHubId(hubId)
                .estimatedDistance(estimatedDistance)
                .estimatedDuration(estimatedDuration)
                .build();
    }
}
