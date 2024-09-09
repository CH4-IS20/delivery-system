package com.sparta.ch4.delivery.company.presentation.controller;


import com.sparta.ch4.delivery.company.application.service.CompanyService;
import com.sparta.ch4.delivery.company.domain.type.CompanySearchType;
import com.sparta.ch4.delivery.company.presentation.request.CompanyCreateRequest;
import com.sparta.ch4.delivery.company.presentation.request.CompanyUpdateRequest;
import com.sparta.ch4.delivery.company.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.company.presentation.response.CompanyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
            @Valid @RequestBody CompanyCreateRequest request,
            @RequestHeader("X-UserId") String userId
    ) {
        return CommonResponse.success(
                CompanyResponse.from(companyService.createCompany(request.toDto(userId)))
        );
    }

    @GetMapping
    public CommonResponse<Page<CompanyResponse>> getCompanies(
            @RequestParam(required = false, name = "searchType") CompanySearchType searchType,
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return CommonResponse.success(
                companyService.getAllCompanies(searchType, searchValue, pageable).map(CompanyResponse::from)
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

    // Header 에 들어온 userId 로 updatedBy 설정
    @PutMapping("/{companyId}")
    public CommonResponse<CompanyResponse> updateCompany(
            @PathVariable(name = "companyId") UUID companyId,
            @Valid @RequestBody CompanyUpdateRequest request,
            @RequestHeader("X-UserId") String userId
    ) {
        return CommonResponse.success(
                CompanyResponse.from(companyService.updateCompany(companyId, request.toDto(userId)))
        );
    }

    // Header 에 들어온 userId 로 deletedBy 설정
    @DeleteMapping("/{companyId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable(name = "companyId") UUID companyId,
            @RequestHeader("X-UserId") String userId
    ) {
        companyService.deleteCompany(companyId, userId);
    }
}
