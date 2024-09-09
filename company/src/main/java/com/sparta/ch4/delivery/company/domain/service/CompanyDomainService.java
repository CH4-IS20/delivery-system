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


    public CompanyDto getCompanyById(UUID id) {
        return companyRepository.findById(id).map(CompanyDto::from).orElseThrow(
                () -> new EntityNotFoundException("ID 에 해당하는 업체가 존재하지 않습니다.")
        );
    }

    public Page<CompanyDto> getAllCompanies(CompanySearchType searchType, String searchValue, Pageable pageable) {
        return companyRepository.searchCompany(searchType, searchValue, pageable).map(CompanyDto::from);
    }

    public CompanyDto updateCompany(UUID companyId, CompanyDto dto) {
        // company 조회
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new EntityNotFoundException("ID 에 해당하는 업체가 존재하지 않습니다.")
        );
        // 업데이트
        company.setHubId(dto.hubId());
        company.setName(dto.name());
        company.setType(dto.type());
        company.setAddress(dto.address());
        // 저장
        return CompanyDto.from(companyRepository.save(company));
    }

    public void deleteCompany(UUID companyId, String userId) {
        // company 조회
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new EntityNotFoundException("ID 에 해당하는 업체가 존재하지 않습니다.")
        );
        // Soft delete
        company.setIsDeleted(true);
        company.setDeletedAt(LocalDateTime.now());
        company.setDeletedBy(userId);
    }
}
