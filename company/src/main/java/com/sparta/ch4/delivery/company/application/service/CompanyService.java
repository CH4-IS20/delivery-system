package com.sparta.ch4.delivery.company.application.service;

import com.sparta.ch4.delivery.company.application.dto.CompanyDto;
import com.sparta.ch4.delivery.company.domain.exception.ApplicationException;
import com.sparta.ch4.delivery.company.domain.model.Company;
import com.sparta.ch4.delivery.company.domain.service.CompanyDomainService;
import com.sparta.ch4.delivery.company.domain.type.CompanySearchType;
import com.sparta.ch4.delivery.company.infrastructure.client.HubClient;
import com.sparta.ch4.delivery.company.infrastructure.client.UserClient;
import com.sparta.ch4.delivery.company.infrastructure.client.response.UserResponse;
import com.sparta.ch4.delivery.company.presentation.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.sparta.ch4.delivery.company.domain.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyDomainService companyDomainService;

    private final UserClient userClient;
    private final HubClient hubClient;


    @Transactional
    public CompanyDto createCompany(CompanyDto dto) {
        if(!hubClient.getHub(dto.hubId()).getData().id().equals(dto.hubId())){
            throw new ApplicationException(HUB_NOT_FOUND);
        };
        return companyDomainService.createCompany(dto);
    }

    @Transactional(readOnly = true)
    public CompanyDto getCompanyById(UUID id) {
        return CompanyDto.from(companyDomainService.getCompanyById(id));
    }

    @Transactional(readOnly = true)
    public Page<CompanyDto> getAllCompanies(CompanySearchType searchType, String searchValue, Pageable pageable) {
        return companyDomainService.getAllCompanies(searchType, searchValue, pageable).map(CompanyDto::from);
    }

    @Transactional
    public CompanyDto updateCompany(UUID companyId, CompanyDto updatingDto, String userId){
        Company company = companyDomainService.getCompanyById(companyId);
        // 허브 검증
        if(!hubClient.getHub(updatingDto.hubId()).getData().id().equals(company.getHubId())){
            throw new ApplicationException(HUB_NOT_FOUND);
        };
        // 유저 권한 검증
        if (!checkAuthForManaging(userId, companyId, updatingDto.hubId())){
            throw new ApplicationException(ACCESS_DENIED);
        }

        return CompanyDto.from(companyDomainService.updateCompany(company, updatingDto));
    }

    @Transactional
    public void deleteCompany(UUID companyId, String userId) {
        Company company = companyDomainService.getCompanyById(companyId);
        // 유저 권한 검증
        if (!checkAuthForManaging(userId, company.getId(), company.getHubId())){
            throw new ApplicationException(ACCESS_DENIED);
        }
        companyDomainService.deleteCompany(company, userId);
    }


    private boolean checkAuthForManaging(String userId, UUID companyId, UUID companyHubId) {
        /* 마스터가 아니라면
        * 1. 허브 관리자 : 자신 허브가 관리하는 업체만 수정 가능
        * 2. 업체 관리자 : 자신의 업체만 수정 가능, 다른 업체의 읽기 검색만 가능
        */
        CommonResponse<UserResponse> userApiRes = userClient.getUser(Long.parseLong(userId));
        UserResponse user = userApiRes.getData();
        return switch (user.role()){
            case MASTER -> true;
            case HUB_MANAGER -> user.hubId().equals(companyHubId);
            case HUB_COMPANY -> user.companyId().equals(companyId);
            case HUB_DELIVERY -> false;
        };
    }

}
