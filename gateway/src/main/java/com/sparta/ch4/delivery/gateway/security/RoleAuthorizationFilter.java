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
        // Reactive Security Context에서 Authentication 가져오기
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    String policy = getPolicy(path);

                    if (policy == null) {
                        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }

                    if (authentication == null || !authentication.isAuthenticated()) {
                        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    if (policy.equals("authenticated")) {
                        return chain.filter(exchange);
                    }

                    boolean hasAccess = authentication.getAuthorities().stream()
                            .anyMatch(grantedAuthority -> policy.contains(grantedAuthority.getAuthority()));

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
