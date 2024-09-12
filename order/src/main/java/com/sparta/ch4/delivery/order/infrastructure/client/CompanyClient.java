package com.sparta.ch4.delivery.order.infrastructure.client;

import com.sparta.ch4.delivery.order.infrastructure.client.response.CompanyResponse;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company")
public interface CompanyClient {

    @GetMapping("/api/companies/{companyId}")
    CommonResponse<CompanyResponse> getCompany(@PathVariable UUID companyId);

}