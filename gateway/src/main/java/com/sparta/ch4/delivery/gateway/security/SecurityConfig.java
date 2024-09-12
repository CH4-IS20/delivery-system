package com.sparta.ch4.delivery.gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.HTTP_BASIC)
                .authorizeExchange(exchanges -> exchanges

                        .pathMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        // 사용자 관련
                        .pathMatchers(HttpMethod.GET, "/api/users/{userId}")
                        .authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/users").hasRole("MASTER") // 사용자 전체 조회
                        .pathMatchers(HttpMethod.PUT, "/api/users/{userId}")
                        .authenticated() // 사용자 정보 수정
                        .pathMatchers(HttpMethod.DELETE, "/api/users/{userId}") // 사용자 삭제
                        .authenticated()

                        // 슬랙 관련
                        .pathMatchers(HttpMethod.GET, "/api/slack").hasRole("MASTER") // 슬랙 메시지 조회
                        .pathMatchers(HttpMethod.POST, "/api/slack").hasRole("MASTER") // 슬랙 메시지 전송

                        // 배송 매니저 관련
                        .pathMatchers(HttpMethod.POST, "/api/delivery-managers")
                        .hasAnyRole("MASTER", "HUB_MANAGER") // 배송담당자 생성
                        .pathMatchers(HttpMethod.GET,
                                "/api/delivery-managers/{delivery_manager_id}")
                        .hasAnyRole("MASTER", "DELIVERY_MANAGER", "HUB_MANAGER") // 배송담당자 단건 조회
                        .pathMatchers(HttpMethod.GET, "/api/delivery-managers")
                        .hasRole("MASTER") // 배송담당자 전체 조회
                        .pathMatchers(HttpMethod.PUT,
                                "/api/delivery-managers/{delivery_manager_id}")
                        .hasAnyRole("MASTER", "HUB_MANAGER") // 배송담당자 수정
                        .pathMatchers(HttpMethod.DELETE,
                                "/api/delivery-managers/{delivery_manager_id}")
                        .hasAnyRole("MASTER", "HUB_MANAGER") // 배송담당자 삭제

                        // 업체 관련
                        .pathMatchers(HttpMethod.POST, "/api/companies").hasRole("MASTER") // 업체 생성
                        .pathMatchers(HttpMethod.PUT, "/api/companies/{company_id}")
                        .hasAnyRole("MASTER", "HUB_MANAGER", "HUB_COMPANY") // 업체 수정
                        .pathMatchers(HttpMethod.GET, "/api/companies/{company_id}")
                        .hasAnyRole("MASTER", "HUB_COMPANY") // 업체 단건 조회
                        .pathMatchers(HttpMethod.GET, "/api/companies").hasAnyRole("MASTER",
                                "HUB_COMPANY") // 업체 전체 조회
                        .pathMatchers(HttpMethod.DELETE, "/api/companies/{company_id}")
                        .hasAnyRole("MASTER",
                                "HUB_MANAGER",
                                "HUB_COMPANY") // 업체 삭제

                        // 상품 관련
                        .pathMatchers(HttpMethod.POST, "/api/products")
                        .hasAnyRole("HUB_MANAGER", "HUB_COMPANY") // 상품 생성
                        .pathMatchers(HttpMethod.PUT, "/api/products/{product_id}").hasAnyRole(
                                "HUB_MANAGER",
                                "HUB_COMPANY") // 상품 수정
                        .pathMatchers(HttpMethod.GET, "/api/products/{product_id}")
                        .authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/products")
                        .authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/api/products/{product_id}").hasAnyRole(
                                "HUB_MANAGER",
                                "HUB_COMPANY") // 상품 삭제

                        // 주문 관련
                        .pathMatchers(HttpMethod.POST, "/api/orders")
                        .authenticated() // 주문 생성
                        .pathMatchers(HttpMethod.PUT, "/api/orders/{order_id}")
                        .authenticated() // 주문 수정
                        .pathMatchers(HttpMethod.GET, "/api/orders/{order_id}")
                        .authenticated() // 주문 단건 조회
                        .pathMatchers(HttpMethod.GET, "/api/orders").authenticated()// 주문 전체 조회
                        .pathMatchers(HttpMethod.DELETE, "/api/orders/{order_id}")
                        .hasAnyRole("MASTER",
                                "HUB_MANAGER") // 주문 삭제

                        // 배송 관련
                        .pathMatchers(HttpMethod.POST, "/api/deliveries")
                        .authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/deliveries/{deliveryId}")
                        .authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/deliveries")
                        .authenticated()
                        .pathMatchers(HttpMethod.PUT, "/api/deliveries/{deliveryId}")
                        .hasAnyRole("MASTER", "DELIVERY_MANAGER",
                                "HUB_MANAGER") // 배송 정보 수정
                        .pathMatchers(HttpMethod.DELETE, "/api/deliveries/{deliveryId}")
                        .hasAnyRole("MASTER", "DELIVERY_MANAGER",
                                "HUB_MANAGER") // 배송 삭제

                        // 허브 관련
                        .pathMatchers(HttpMethod.POST, "/api/hubs").hasRole("MASTER") // 허브 생성
                        .pathMatchers(HttpMethod.GET, "/api/hubs/{hubId}") // 허브 단건 조회
                        .authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/hubs") // 허브 검색
                        .authenticated()
                        .pathMatchers(HttpMethod.PATCH, "/api/hubs/{hubId}")
                        .hasRole("MASTER") // 허브 정보 수정
                        .pathMatchers(HttpMethod.DELETE, "/api/hubs/{hubId}").hasRole("MASTER") // 허브 삭제

                        // 허브 이동 관리 관련
                        .pathMatchers(HttpMethod.POST, "/api/hub-routes")
                        .hasRole("MASTER") // 허브이동관리 생성
                        .pathMatchers(HttpMethod.GET, "/api/hub-routes/{hubRouteId}")
                        .authenticated() // 허브 이동 관리 단건 조회
                        .pathMatchers(HttpMethod.GET, "/api/hub-routes")
                        .authenticated() // 허브 이동 관리 검색
                        .pathMatchers(HttpMethod.PUT, "/api/hub-routes/{hubRouteId}")
                        .hasRole("MASTER") // 허브이동관리 수정
                        .pathMatchers(HttpMethod.DELETE, "/api/hub-routes/{hubRouteId}")
                        .hasRole("MASTER") // 허브이동관리 삭제
                );
        return http.build();
    }

}
