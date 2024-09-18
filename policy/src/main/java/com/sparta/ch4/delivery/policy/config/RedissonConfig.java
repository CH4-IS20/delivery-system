package com.sparta.ch4.delivery.policy.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.redisson.config.Config;

@Configuration
public class RedissonConfig {

    @Value("${redisson.url}")
    private String redissonUrl;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(redissonUrl);
        config.setCodec(new SerializationCodec());
        return Redisson.create(config);
    }
}
