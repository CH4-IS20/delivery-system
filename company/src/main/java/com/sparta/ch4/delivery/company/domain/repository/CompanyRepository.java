package com.sparta.ch4.delivery.company.domain.repository;

import com.sparta.ch4.delivery.company.domain.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID>, CompanyRepositoryCustom {
}
