package com.sparta.ch4.delivery.company.presentation.controller;


import com.sparta.ch4.delivery.company.application.service.CompanyService;
import com.sparta.ch4.delivery.company.domain.type.CompanySearchType;
import com.sparta.ch4.delivery.company.presentation.request.CompanyCreateRequest;
import com.sparta.ch4.delivery.company.presentation.request.CompanyUpdateRequest;
import com.sparta.ch4.delivery.company.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.company.presentation.response.CompanyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "Company API", description = "업체 CRUD")
public class CompanyController {

    private final CompanyService companyService;

    // Header 에 들어온 userId 로 createdBy 설정
    @PostMapping
    @Operation(summary = "업체 생성", description = "업체 생성 API")
    public CommonResponse<CompanyResponse> createCompany(
            @Valid @RequestBody CompanyCreateRequest request,
            @RequestHeader("X-UserId") String userId
    ) {
        return CommonResponse.success(
                CompanyResponse.from(companyService.createCompany(request.toDto(userId)))
        );
    }

    @GetMapping
    @Operation(summary = "업체 리스트(페이징) 조회", description = "모든 업체 조회 API")
    public CommonResponse<Page<CompanyResponse>> getCompanies(
            @Parameter(name = "searchType", description = "검색 조건 (Enum) 설정", schema = @Schema(implementation = CompanySearchType.class))
            @RequestParam(required = false, name = "searchType") CompanySearchType searchType,
            @Parameter(name = "searchValue", description = "검색 조건 기준으로 한 검색어")
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @ParameterObject @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return CommonResponse.success(
                companyService.getAllCompanies(searchType, searchValue, pageable).map(CompanyResponse::from)
        );
    }


    @GetMapping("/{companyId}")
    @Operation(summary = "업체 단건 조회", description = "업체 단건 조회 API")
    public CommonResponse<CompanyResponse> getCompany(
            @PathVariable(name = "companyId") UUID companyId
    ) {
        return CommonResponse.success(
                CompanyResponse.from(companyService.getCompanyById(companyId))
        );
    }

    // Header 에 들어온 userId 로 updatedBy 설정
    @PutMapping("/{companyId}")
    @Operation(summary = "업체 정보 수정", description = "업체 수정 API (관리허브, 이름, 주소, 업체 타입)")
    public CommonResponse<CompanyResponse> updateCompany(
            @PathVariable(name = "companyId") UUID companyId,
            @Valid @RequestBody CompanyUpdateRequest request,
            @RequestHeader("X-UserId") String userId
    ) {
        return CommonResponse.success(
                CompanyResponse.from(companyService.updateCompany(companyId, request.toDto(userId), userId))
        );
    }

    // Header 에 들어온 userId 로 deletedBy 설정
    @DeleteMapping("/{companyId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "업체 정보 삭제", description = "업체 삭제 API (논리적인 삭제 처리)")
    public void delete(
            @PathVariable(name = "companyId") UUID companyId,
            @RequestHeader("X-UserId") String userId
    ) {
        companyService.deleteCompany(companyId, userId);
    }


}
