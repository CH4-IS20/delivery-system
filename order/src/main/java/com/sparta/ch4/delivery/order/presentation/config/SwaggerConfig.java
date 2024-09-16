package com.sparta.ch4.delivery.order.presentation.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.models.security.SecurityScheme.*;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customizeOpenAPI() {

        Info info = new Info()
                .title("DeliveryProject")
                .description("✨ MSA를 통한 허브 물류 관리 시스템 ✨");


        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer",
                        new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(Type.HTTP)
                                .in(In.HEADER)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement()
                        .addList("Bearer"))
                .addServersItem(new Server().url("/"))
                .info(info);
    }
}