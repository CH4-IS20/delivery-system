package com.sparta.ch4.delivery.gateway.security;

import jakarta.annotation.Nonnull;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class RoleAuthorizationFilter implements WebFilter {

    private final RedissonClient redissonClient;

    private static final String POLICY_CACHE_KEY = "endpointPolicyCache";

    public RoleAuthorizationFilter(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public @Nonnull Mono<Void> filter(ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (exchange.getRequest().getURI().getPath().equals("/api/auth/login")
                || exchange.getRequest().getURI().getPath().equals("/api/auth/register")) {
            return chain.filter(exchange);
        }
        // Reactive Security Context 에서 Authentication 가져오기
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    // 해당 경로에 대한 정책을 Redis 에서 가져옴
                    String policy = getPolicy(path);

                    // 정책이 없으면 접근이 금지됨 (403 Forbidden)
                    if (policy == null) {
                        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }

                    // 인증 정보가 없거나 인증되지 않은 사용자인 경우 접근이 금지됨 (401 Unauthorized)
                    if (authentication == null || !authentication.isAuthenticated()) {
                        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    // 정책이 "authenticated"로 설정된 경우 인증된 사용자만 통과
                    if (policy.equals("authenticated")) {
                        return chain.filter(exchange);
                    }

                    // 인증된 사용자의 권한 중 하나가 정책에 맞는지 확인
                    boolean hasAccess = authentication.getAuthorities().stream()
                            .anyMatch(grantedAuthority -> policy.contains(grantedAuthority.getAuthority()));

                    // 권한이 맞지 않으면 접근이 금지됨 (403 Forbidden)
                    if (!hasAccess) {
                        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }

                    return chain.filter(exchange);
                });

    }

    // 정책을 조회
    public String getPolicy(String endpoint) {
        RMapCache<String, String> policyCache = redissonClient.getMapCache(POLICY_CACHE_KEY);
        return policyCache.get(endpoint);
    }
}
