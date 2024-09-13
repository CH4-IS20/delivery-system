package com.sparta.ch4.delivery.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customizeOpenAPI() {
        Info info = new Info()
                .title("DeliveryProject")
                .description("✨ MSA를 통한 허브 물류 관리 시스템 ✨");
        return new OpenAPI()
                .info(info);
    }

}