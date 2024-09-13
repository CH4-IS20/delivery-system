package com.sparta.ch4.delivery.policy.service;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class PolicyService {

    private final RedissonClient redissonClient;
    private static final String POLICY_CACHE_KEY = "endpointPolicyCache";

    public PolicyService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    // 권한 정책 초기화
    public void initPolicies() {
        RMapCache<String, String> policyCache = redissonClient.getMapCache(POLICY_CACHE_KEY);

        // 엔드포인트별 권한 정책을 정의 (예: 엔드포인트 -> 권한)
        Map<String, String> policies = new HashMap<>();

        // 사용자 관련 엔드포인트
        policies.put("/api/users/{userId}", "authenticated");
        policies.put("/api/users", "ROLE_MASTER");
        policies.put("/api/users/{userId}", "authenticated");  // 사용자 정보 수정
        policies.put("/api/users/{userId}/delete", "authenticated");  // 사용자 삭제

        // 슬랙 관련 엔드포인트
        policies.put("/api/slack", "ROLE_MASTER");
        policies.put("/api/slack/send", "ROLE_MASTER");

        // 배송 매니저 관련 엔드포인트
        policies.put("/api/delivery-managers", "ROLE_MASTER,ROLE_HUB_MANAGER");
        policies.put("/api/delivery-managers/{delivery_manager_id}", "ROLE_MASTER,ROLE_DELIVERY_MANAGER,ROLE_HUB_MANAGER");
        policies.put("/api/delivery-managers/all", "ROLE_MASTER");
        policies.put("/api/delivery-managers/{delivery_manager_id}/update", "ROLE_MASTER,ROLE_HUB_MANAGER");
        policies.put("/api/delivery-managers/{delivery_manager_id}/delete", "ROLE_MASTER,ROLE_HUB_MANAGER");

        // 업체 관련 엔드포인트
        policies.put("/api/companies", "ROLE_MASTER");
        policies.put("/api/companies/{company_id}", "ROLE_MASTER,ROLE_HUB_MANAGER,ROLE_HUB_COMPANY");
        policies.put("/api/companies/all", "ROLE_MASTER,ROLE_HUB_COMPANY");
        policies.put("/api/companies/{company_id}/delete", "ROLE_MASTER,ROLE_HUB_MANAGER,ROLE_HUB_COMPANY");

        // 상품 관련 엔드포인트
        policies.put("/api/products", "ROLE_HUB_MANAGER,ROLE_HUB_COMPANY");
        policies.put("/api/products/{product_id}", "authenticated");
        policies.put("/api/products/{product_id}/update", "ROLE_HUB_MANAGER,ROLE_HUB_COMPANY");
        policies.put("/api/products/{product_id}/delete", "ROLE_HUB_MANAGER,ROLE_HUB_COMPANY");

        // 주문 관련 엔드포인트
        policies.put("/api/orders", "authenticated");
        policies.put("/api/orders/{order_id}", "authenticated");
        policies.put("/api/orders/all", "authenticated");
        policies.put("/api/orders/{order_id}/delete", "ROLE_MASTER,ROLE_HUB_MANAGER");

        // 배송 관련 엔드포인트
        policies.put("/api/deliveries", "authenticated");
        policies.put("/api/deliveries/{deliveryId}", "authenticated");
        policies.put("/api/deliveries/all", "authenticated");
        policies.put("/api/deliveries/{deliveryId}/update", "ROLE_MASTER,ROLE_DELIVERY_MANAGER,ROLE_HUB_MANAGER");
        policies.put("/api/deliveries/{deliveryId}/delete", "ROLE_MASTER,ROLE_DELIVERY_MANAGER,ROLE_HUB_MANAGER");

        // 허브 관련 엔드포인트
        policies.put("/api/hubs", "ROLE_MASTER");
        policies.put("/api/hubs/{hubId}", "authenticated");
        policies.put("/api/hubs/all", "authenticated");
        policies.put("/api/hubs/{hubId}/update", "ROLE_MASTER");
        policies.put("/api/hubs/{hubId}/delete", "ROLE_MASTER");

        // 허브 이동 관리 관련 엔드포인트
        policies.put("/api/hub-routes", "ROLE_MASTER");
        policies.put("/api/hub-routes/{hubRouteId}", "authenticated");
        policies.put("/api/hub-routes/all", "authenticated");
        policies.put("/api/hub-routes/{hubRouteId}/update", "ROLE_MASTER");
        policies.put("/api/hub-routes/{hubRouteId}/delete", "ROLE_MASTER");

        // 정책을 Redis에 저장하며 1일 동안 캐시 유지
        policies.forEach((endpoint, role) -> policyCache.put(endpoint, role, 1, TimeUnit.DAYS));
    }

    // 매 자정에 권한 정책을 갱신하는 스케줄러
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    public void refreshPolicies() {
        initPolicies(); // 정책을 다시 초기화하여 갱신
    }
}

