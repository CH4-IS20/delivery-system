package com.example.hub.infrastructure.config;

import com.example.hub.infrastructure.ai.GeminiInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestClient geminiRestClient(@Value("${gemini.baseurl}") String baseUrl,
                                       @Value("${googleai.api.key}") String apikey) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-goog-api-key",apikey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept","application/json")
                .build();
    }


    @Bean
    public GeminiInterface geminiInterface(@Qualifier("geminiRestClient") RestClient restClient) {
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(GeminiInterface.class);
    }
}