package com.sparta.ch4.delivery.gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final RoleAuthorizationFilter roleAuthorizationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(roleAuthorizationFilter, SecurityWebFiltersOrder.AUTHORIZATION)
                .authorizeExchange(exchanges -> exchanges
                        // FIXME: swagger ui에 접근은 되는데 정보를 불러오려 하면 인증을 요구함
//                        .pathMatchers("/v3/api-docs/**", "/swagger-ui.html", "/webjars/**").permitAll()
                .anyExchange().permitAll()
                );
        return http.build();
    }

}
