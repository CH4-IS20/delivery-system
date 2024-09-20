package com.sparta.ch4.delivery.company.infrastructure.client;


import com.sparta.ch4.delivery.company.infrastructure.client.response.HubResponse;
import com.sparta.ch4.delivery.company.presentation.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub")
public interface HubClient {
    @GetMapping("/api/hubs/{hubId}")
    CommonResponse<HubResponse> getHub(@PathVariable(name = "hubId") UUID id);
}
