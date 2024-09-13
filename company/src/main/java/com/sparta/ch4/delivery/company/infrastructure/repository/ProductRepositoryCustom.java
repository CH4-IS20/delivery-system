package com.sparta.ch4.delivery.company.infrastructure.repository;

import com.sparta.ch4.delivery.company.domain.model.Product;
import com.sparta.ch4.delivery.company.domain.type.ProductSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductRepositoryCustom {

    Page<Product> searchProducts(UUID companyId, UUID hubId, ProductSearchType searchType, String searchValue, Pageable pageable);
}
