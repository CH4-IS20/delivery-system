package com.sparta.ch4.delivery.company.application.service;

import com.sparta.ch4.delivery.company.application.dto.CompanyDto;
import com.sparta.ch4.delivery.company.application.dto.ProductDto;
import com.sparta.ch4.delivery.company.domain.service.ProductDomainService;
import com.sparta.ch4.delivery.company.domain.type.CompanySearchType;
import com.sparta.ch4.delivery.company.domain.type.ProductSearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductDomainService productDomainService;


    // TODO : 상품 관리 허브가 존재하는 지 확인
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        return productDomainService.createProduct(productDto);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID productId) {
        return productDomainService.getProductById(productId);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(UUID companyId, UUID hubId, ProductSearchType searchType, String searchValue, Pageable pageable) {
        return productDomainService.getAllProducts(companyId, hubId, searchType, searchValue, pageable);
    }

    // TODO : 상품 관리 허브가 존재하는 지 확인
    @Transactional
    public ProductDto updateProduct(UUID productId, ProductDto dto) {
        return productDomainService.updateProduct(productId, dto);
    }

    @Transactional
    public void deleteProduct(UUID productId, String userId) {
        productDomainService.deleteProduct(productId, userId);
    }
}