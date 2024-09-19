package com.sparta.ch4.delivery.gateway.security;

import com.sparta.ch4.delivery.gateway.response.ResponseWriter;
import jakarta.annotation.Nonnull;
import java.util.Map;
import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class RoleAuthorizationFilter implements WebFilter {

    private final RedissonReactiveClient redissonClient;

    private final ResponseWriter responseWriter;

    private static final String POLICY_CACHE_KEY = "endpointPolicyCache";

    public RoleAuthorizationFilter(RedissonReactiveClient redissonClient,
            ResponseWriter responseWriter) {
        this.redissonClient = redissonClient;
        this.responseWriter = responseWriter;
    }

    @Override
    public @Nonnull Mono<Void> filter(ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = String.valueOf(exchange.getRequest().getMethod());

        // Swagger UI 및 관련 리소스 경로, 각 서비스의 API 문서 경로는 필터 제외
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // 로그인, 회원가입 엔드포인트는 권한 체크 제외
        if (path.equals("/api/auth/login") || path.equals("/api/auth/register")) {
            return chain.filter(exchange);
        }

        // Reactive Security Context에서 Authentication 가져오기
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication ->
                        // 해당 경로와 메서드에 대한 정책을 Reactive 방식으로 Redis에서 가져옴
                        getPolicyWithRegex(path, method)
                                .flatMap(policy -> {

                                    // 정책이 없으면 접근이 금지됨 (403 Forbidden)
                                    if (policy == null) {
                                        return responseWriter.writeResponse(exchange, HttpStatus.FORBIDDEN,
                                                "정책이 설정되지 않았습니다.");
                                    }

                                    // 인증 정보가 없거나 인증되지 않은 사용자인 경우 접근 금지 (401 Unauthorized)
                                    if (!authentication.isAuthenticated()) {
                                        return responseWriter.writeResponse(exchange, HttpStatus.UNAUTHORIZED,
                                                "인증이 필요합니다.");
                                    }

                                    // MASTER 권한은 통과
                                    if (authentication.getAuthorities().stream()
                                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MASTER"))) {
                                        return chain.filter(exchange);
                                    }

                                    // 정책이 "authenticated"인 경우 인증된 사용자만 통과 허용
                                    if (policy.equals("authenticated")) {
                                        return chain.filter(exchange);
                                    }

                                    // 사용자의 권한 중 하나가 정책과 일치하는지 확인
                                    boolean hasAccess = authentication.getAuthorities().stream()
                                            .anyMatch(grantedAuthority -> policy.contains(
                                                    grantedAuthority.getAuthority()));

                                    // 사용자 권한이 정책과 일치하지 않으면 접근 금지 (403 Forbidden)
                                    if (!hasAccess) {
                                        return responseWriter.writeResponse(exchange, HttpStatus.FORBIDDEN,
                                                "권한이 부족합니다.");
                                    }

                                    // 권한이 맞으면 필터 체인 계속 진행
                                    return chain.filter(exchange);
                                }));

    }

    public Mono<String> getPolicyWithRegex(String path, String method) {
        RMapCacheReactive<String, Map<String, String>> policyCache = redissonClient.getMapCache(
                POLICY_CACHE_KEY);

        // 다양한 경로 변수들을 처리할 수 있는 정규식을 사용하여 경로 일반화 (e.g. /api/users/1 -> /api/users/{id})
        String normalizedPath = path.replaceAll("/\\d+$", "/{id}");

        // 일반화된 경로로 Redis에서 정책 조회
        return policyCache.get(normalizedPath)
                .flatMap(methodRoles -> Mono.justOrEmpty(methodRoles.get(method)));
    }


    private boolean isPublicPath(String path) {
        return path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/webjars/swagger-ui") ||
                path.equals("/favicon.ico") ||
                path.startsWith("/users/v3/api-docs") ||
                path.startsWith("/companies/v3/api-docs") ||
                path.startsWith("/hubs/v3/api-docs") ||
                path.startsWith("/orders/v3/api-docs");
    }

}
