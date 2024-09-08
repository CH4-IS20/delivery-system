package com.sparta.ch4.delivery.company.application.service;

import com.sparta.ch4.delivery.company.application.dto.ProductDto;
import com.sparta.ch4.delivery.company.domain.service.ProductDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductDomainService productDomainService;


    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        return productDomainService.createProduct(productDto);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID productId) {
        return productDomainService.getProductById(productId);
    }

}