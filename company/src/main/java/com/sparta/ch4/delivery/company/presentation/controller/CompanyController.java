package com.sparta.ch4.delivery.company.presentation.controller;


import com.sparta.ch4.delivery.company.application.service.CompanyService;
import com.sparta.ch4.delivery.company.presentation.request.CompanyCreateRequest;
import com.sparta.ch4.delivery.company.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.company.presentation.response.CompanyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    // Header 에 들어온 userId 로 createdBy 설정
    @PostMapping
    public CommonResponse<CompanyResponse> createCompany(
            @RequestBody CompanyCreateRequest request,
            @RequestHeader("X-UserId") String userId
    ) {
        return CommonResponse.success(
                CompanyResponse.from(companyService.createCompany(request.toDto(userId)))
        );
    }

    @GetMapping("/{companyId}")
    public CommonResponse<CompanyResponse> getCompany(
            @PathVariable(name = "companyId") UUID companyId
            ) {
        return CommonResponse.success(
                CompanyResponse.from(companyService.getCompanyById(companyId))
        );
    }

}
