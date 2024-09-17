package com.sparta.ch4.delivery.gateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Override
    public @Nonnull Mono<Void> filter(ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
        if (exchange.getRequest().getURI().getPath().equals("/api/auth/login")
                || exchange.getRequest().getURI().getPath().equals("/api/auth/register")) {
            return chain.filter(exchange);
        }

        HttpHeaders headers = exchange.getRequest().getHeaders();
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // jwt 검증
            Claims claims;
            try {
                claims = verifyJwtToken(token, secretKey);
            } catch (Exception e) {
                return Mono.error(new RuntimeException("유효하지 않은 JWT 토큰입니다."));
            }

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            // X-UserId 헤더 추가
            ServerHttpRequest modifiedRequest = addUserIdHeader(exchange.getRequest(), userId);

            // X-Role 헤더 추가
            modifiedRequest = addRoleHeader(modifiedRequest, role);

            // role로 Authentication 객체 생성
            Authentication authentication = createAuthenticationFromRole(userId, role);

            // 요청을 수정하여 ServerWebExchange에 적용
            ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

            return chain.filter(modifiedExchange).contextWrite(
                    ReactiveSecurityContextHolder.withAuthentication(authentication));
        }
        return chain.filter(exchange);
    }

    private Claims verifyJwtToken(String token, String secretKey) {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        var secretKeyBytes = Keys.hmacShaKeyFor(bytes);

        return Jwts.parserBuilder()
                .setSigningKey(secretKeyBytes)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private ServerHttpRequest addUserIdHeader(ServerHttpRequest request, String userId) {
        final String USER_ID_HEADER = "X-UserId";
        return request.mutate()
                .header(USER_ID_HEADER, userId)
                .build();
    }

    private ServerHttpRequest addRoleHeader(ServerHttpRequest request, String role) {
        final String X_ROLE_HEADER = "X-Role";
        return request.mutate()
                .header(X_ROLE_HEADER, role)
                .build();
    }

    private Authentication createAuthenticationFromRole(String userId, String role) {
        final String ROLE_PREFIX = "ROLE_";
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(ROLE_PREFIX + role));
        return new UsernamePasswordAuthenticationToken(userId, null, authorities);
    }
}
