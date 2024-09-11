package com.sparta.ch4.delivery.company.application.service;

import com.sparta.ch4.delivery.company.application.dto.CompanyDto;
import com.sparta.ch4.delivery.company.application.dto.CompanyWithUserDto;
import com.sparta.ch4.delivery.company.domain.model.Company;
import com.sparta.ch4.delivery.company.domain.service.CompanyDomainService;
import com.sparta.ch4.delivery.company.domain.type.CompanySearchType;
import com.sparta.ch4.delivery.company.infrastructure.client.UserClient;
import com.sparta.ch4.delivery.company.infrastructure.client.response.UserResponse;
import com.sparta.ch4.delivery.company.presentation.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyDomainService companyDomainService;

    private final UserClient userClient;


    //TODO : Hub 조회 및 검증 로직 작성
    @Transactional
    public CompanyDto createCompany(CompanyDto dto) {
        return companyDomainService.createCompany(dto);
    }

    @Transactional(readOnly = true)
    public CompanyDto getCompanyById(UUID id) {
        return companyDomainService.getCompanyById(id);
    }

    @Transactional(readOnly = true)
    public Page<CompanyDto> getAllCompanies(CompanySearchType searchType, String searchValue, Pageable pageable) {
        return companyDomainService.getAllCompanies(searchType, searchValue, pageable);
    }

    //TODO : Hub 조회 및 검증 로직 작성
    @Transactional
    public CompanyDto updateCompany(UUID companyId, CompanyDto dto) {
        return companyDomainService.updateCompany(companyId, dto);
    }

    @Transactional
    public void deleteCompany(UUID companyId, String userId) {
        companyDomainService.deleteCompany(companyId, userId);
    }

    @Transactional
    public CompanyWithUserDto getCompanyAndUserForOrder(UUID companyId) {
        CompanyDto companyDto = companyDomainService.getCompanyById(companyId);
        CommonResponse<UserResponse> userApiResponse = userClient.getOrderRecipientInCompany(companyId);
        UserResponse user = userApiResponse.getData();

        //TODO : user 도메인 쪽 slackId 추가
        return CompanyWithUserDto.fromDtoAndUserInfo(companyDto, user.username(),
                user.slackId()  // 주문 수령자 slackID
        );
    }
}
