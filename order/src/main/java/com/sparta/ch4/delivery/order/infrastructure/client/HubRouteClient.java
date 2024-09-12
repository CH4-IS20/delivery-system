package com.sparta.ch4.delivery.order.infrastructure.client;


import com.sparta.ch4.delivery.order.infrastructure.client.response.HubRouteForOrderResponse;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "hub")
public interface HubRouteClient {

    //TODO : 배송 중이 아닌 배송 담당자, 혹은 후에 배송이 가능한 담당자 배정
    @GetMapping("/api/hubs")
    CommonResponse<List<HubRouteForOrderResponse>> getHubRouteForOrder(
            @RequestParam(name = "supplierId") UUID supplierId,
            @RequestParam(name = "receiverId") UUID receiverId
    );
}
