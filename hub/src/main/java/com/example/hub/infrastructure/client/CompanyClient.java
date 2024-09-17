package com.example.hub.infrastructure.client;

import com.example.hub.presentation.response.CommonResponse;
import com.example.hub.presentation.response.CompanyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company")
public interface CompanyClient {

    @GetMapping("/api/companies/{companyId}")
    CommonResponse<CompanyResponse> getCompany(@PathVariable UUID companyId);

}