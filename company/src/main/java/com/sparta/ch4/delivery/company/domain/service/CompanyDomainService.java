package com.sparta.ch4.delivery.company.domain.service;


import com.sparta.ch4.delivery.company.application.dto.CompanyDto;
import com.sparta.ch4.delivery.company.domain.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CompanyDomainService {

    private final CompanyRepository companyRepository;

    public CompanyDto createCompany(CompanyDto dto) {
        return CompanyDto.from(companyRepository.save(dto.toEntity()));
    }

    //TODO : 에러 내용 정의
    public CompanyDto getCompanyById(UUID id) {
        return companyRepository.findById(id).map(CompanyDto::from).orElseThrow(
                () -> new EntityNotFoundException("ID 에 해당하는 업체가 존재하지 않습니다.")
        );
    }
}
