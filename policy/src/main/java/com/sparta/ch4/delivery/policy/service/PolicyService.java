package com.sparta.ch4.delivery.policy.service;

import jakarta.annotation.PostConstruct;
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
    @PostConstruct
    public void initPolicies() {
        RMapCache<String, Map<String, String>> policyCache = redissonClient.getMapCache(
                POLICY_CACHE_KEY);

        // 엔드포인트별 권한 정책을 정의 (예: 엔드포인트 -> {메서드 -> 권한})
        Map<String, Map<String, String>> policies = new HashMap<>();

// 사용자 관련 엔드포인트
        policies.put("/api/users/{id}", Map.of("GET", "authenticated", "DELETE", "authenticated"));
        policies.put("/api/users", Map.of("GET", "ROLE_MASTER", "POST", "ROLE_MASTER"));

// 슬랙 관련 엔드포인트
        policies.put("/api/slack", Map.of("POST", "ROLE_MASTER"));

// 배송 매니저 관련 엔드포인트
        policies.put("/api/delivery-managers",
                Map.of("GET", "ROLE_MASTER,ROLE_HUB_MANAGER", "POST", "ROLE_MASTER"));
        policies.put("/api/delivery-managers/{id}", Map.of(
                "GET", "ROLE_MASTER,ROLE_HUB_DELIVERY,ROLE_HUB_MANAGER",
                "PUT", "ROLE_MASTER,ROLE_HUB_MANAGER",
                "DELETE", "ROLE_MASTER,ROLE_HUB_MANAGER"
        ));

// 업체 관련 엔드포인트
        policies.put("/api/companies", Map.of("POST", "ROLE_MASTER"));
        policies.put("/api/companies/{id}", Map.of(
                "GET", "ROLE_MASTER,ROLE_HUB_MANAGER,ROLE_HUB_COMPANY",
                "DELETE", "ROLE_MASTER,ROLE_HUB_MANAGER,ROLE_HUB_COMPANY"
        ));
        policies.put("/api/companies/all", Map.of("GET", "ROLE_MASTER,ROLE_HUB_COMPANY"));

// 상품 관련 엔드포인트
        policies.put("/api/products", Map.of("GET", "ROLE_HUB_MANAGER,ROLE_HUB_COMPANY", "POST",
                "ROLE_HUB_MANAGER,ROLE_HUB_COMPANY"));
        policies.put("/api/products/{id}",
                Map.of("GET", "authenticated", "PUT", "ROLE_HUB_MANAGER,ROLE_HUB_COMPANY", "DELETE",
                        "ROLE_HUB_MANAGER,ROLE_HUB_COMPANY"));

// 주문 관련 엔드포인트
        policies.put("/api/orders", Map.of("GET", "authenticated", "POST", "authenticated"));
        policies.put("/api/orders/{id}",
                Map.of("GET", "authenticated", "DELETE", "ROLE_MASTER,ROLE_HUB_MANAGER"));

// 배송 관련 엔드포인트
        policies.put("/api/deliveries", Map.of("GET", "authenticated", "POST", "authenticated"));
        policies.put("/api/deliveries/{id}", Map.of(
                "GET", "authenticated",
                "PUT", "ROLE_MASTER,ROLE_HUB_DELIVERY,ROLE_HUB_MANAGER",
                "DELETE", "ROLE_MASTER,ROLE_HUB_DELIVERY,ROLE_HUB_MANAGER"
        ));

// 허브 관련 엔드포인트
        policies.put("/api/hubs", Map.of("POST", "ROLE_MASTER", "GET", "authenticated"));
        policies.put("/api/hubs/{id}",
                Map.of("GET", "authenticated", "PUT", "ROLE_MASTER", "DELETE", "ROLE_MASTER"));
        policies.put("/api/hubs/order", Map.of("GET", "authenticated"));

// 허브 이동 관리 관련 엔드포인트
        policies.put("/api/hub-routes", Map.of("GET", "ROLE_MASTER", "DELETE", "ROLE_MASTER"));
        policies.put("/api/hub-routes/list", Map.of("GET", "authenticated"));

        policies.put("/api/policies/update", Map.of("GET", "ROLE_MASTER"));

        // 기존 캐시 데이터를 초기화
        policyCache.clear();

        // 정책을 Redis에 저장하며 1일 동안 캐시 유지
        // FIXME: 실제로는 1일 동안 저장되지 않고 사라집니다. 따라서 cron을 10분 간격으로 설정합니다.
        policies.forEach((endpoint, methodRoles) -> policyCache.put(endpoint, methodRoles, 1,
                TimeUnit.DAYS, 1,
                TimeUnit.DAYS));
    }

    // 매 자정에 권한 정책을 갱신하는 스케줄러
    @Scheduled(cron = "0 */10 * * * ?") // 매 10분마다 실행
    public void refreshPolicies() {
        initPolicies(); // 정책을 다시 초기화하여 갱신
    }
}

