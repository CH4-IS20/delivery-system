package com.sparta.ch4.delivery.company.domain.service;

import com.sparta.ch4.delivery.company.application.dto.ProductDto;
import com.sparta.ch4.delivery.company.domain.model.Company;
import com.sparta.ch4.delivery.company.domain.repository.CompanyRepository;
import com.sparta.ch4.delivery.company.domain.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductDomainService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    //TODO : 에러 내용 정의
    public ProductDto createProduct(ProductDto productDto) {
        Company company = companyRepository.findById(productDto.companyId()).orElseThrow(
                () -> new EntityNotFoundException("ID 에 해당하는 업체를 찾을 수 없습니다.")
        );

        return ProductDto.from(productRepository.save(productDto.toEntity(company)));
    }

    //TODO : 에러 내용 정의
    public ProductDto getProductById(UUID productId) {
        return productRepository.findById(productId).map(ProductDto::from).orElseThrow(
                () -> new EntityNotFoundException("ID 에 해당하는 상품을 찾을 수 없습니다.")
        );
    }

}