package com.sparta.ch4.delivery.company.domain.service;


import com.sparta.ch4.delivery.company.application.dto.CompanyDto;
import com.sparta.ch4.delivery.company.domain.model.Company;
import com.sparta.ch4.delivery.company.domain.repository.CompanyRepository;
import com.sparta.ch4.delivery.company.domain.type.CompanySearchType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

//TODO : 에러 내용 정의
@RequiredArgsConstructor
@Service
public class CompanyDomainService {

    private final CompanyRepository companyRepository;

    public CompanyDto createCompany(CompanyDto dto) {
        return CompanyDto.from(companyRepository.save(dto.toEntity()));
    }


    public Company getCompanyById(UUID id) {
        return companyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ID 에 해당하는 업체가 존재하지 않습니다.")
        );
    }

    public Page<Company> getAllCompanies(CompanySearchType searchType, String searchValue, Pageable pageable) {
        return companyRepository.searchCompanies(searchType, searchValue, pageable);
    }

    public Company updateCompany(Company company, CompanyDto updatingDto) {
        // 업데이트
        company.setHubId(updatingDto.hubId());
        company.setName(updatingDto.name());
        company.setType(updatingDto.type());
        company.setAddress(updatingDto.address());
        company.setUpdatedBy(updatingDto.updatedBy());
        // 저장
        return companyRepository.save(company);
    }

    public void deleteCompany(Company company, String userId) {
        // Soft delete
        company.setIsDeleted(true);
        company.setDeletedAt(LocalDateTime.now());
        company.setDeletedBy(userId);
    }
}
