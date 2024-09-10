package com.sparta.ch4.delivery.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableFeignClients
@EnableSpringDataWebSupport(
		pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

}
