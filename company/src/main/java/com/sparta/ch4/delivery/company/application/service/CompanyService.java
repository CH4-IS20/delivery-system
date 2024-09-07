package com.sparta.ch4.delivery.company.application.service;

import com.sparta.ch4.delivery.company.application.dto.CompanyDto;
import com.sparta.ch4.delivery.company.domain.service.CompanyDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyDomainService companyDomainService;

    public CompanyDto createCompany(CompanyDto dto) {
        return companyDomainService.createCompany(dto);
    }

    public CompanyDto getCompanyById(UUID id) {
        return companyDomainService.getCompanyById(id);
    }

}
