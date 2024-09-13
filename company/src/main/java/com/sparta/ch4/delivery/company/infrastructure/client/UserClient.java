package com.sparta.ch4.delivery.company.infrastructure.client;


import com.sparta.ch4.delivery.company.infrastructure.client.response.UserResponse;
import com.sparta.ch4.delivery.company.presentation.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserClient {
    @GetMapping("/api/users/{userId}")
    CommonResponse<UserResponse> getUser(@PathVariable Long userId);
}
