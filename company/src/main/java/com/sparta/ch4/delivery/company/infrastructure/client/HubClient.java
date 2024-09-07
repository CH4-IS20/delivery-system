package com.sparta.ch4.delivery.company.infrastructure.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "hub")
public interface HubClient {
    //TODO : hub 쪽 생성되면 수정하기
//    @GetMapping("/api/hubs/{hubId}")
//    Optional<HubDto> getHubById(UUID hubId){

//    }

}
