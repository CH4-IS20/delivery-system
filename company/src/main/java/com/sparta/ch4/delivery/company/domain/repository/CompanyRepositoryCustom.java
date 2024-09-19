package com.sparta.ch4.delivery.company.domain.repository;

import com.sparta.ch4.delivery.company.domain.model.Company;
import com.sparta.ch4.delivery.company.domain.type.CompanySearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryCustom {
    Page<Company> searchCompanies(CompanySearchType searchType, String searchValue, Pageable pageable);
}
