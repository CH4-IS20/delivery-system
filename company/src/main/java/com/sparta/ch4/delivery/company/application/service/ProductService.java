package com.sparta.ch4.delivery.company.application.service;

import com.sparta.ch4.delivery.company.application.dto.ProductDto;
import com.sparta.ch4.delivery.company.domain.service.ProductDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductDomainService productDomainService;


    public ProductDto createProduct(ProductDto productDto) {
        return productDomainService.createProduct(productDto);
    }

    public ProductDto getProductById(UUID productId) {
        return productDomainService.getProductById(productId);
    }

}