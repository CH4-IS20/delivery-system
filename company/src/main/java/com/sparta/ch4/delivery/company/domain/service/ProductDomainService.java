package com.sparta.ch4.delivery.company.domain.service;

import com.sparta.ch4.delivery.company.application.dto.ProductDto;
import com.sparta.ch4.delivery.company.domain.model.Company;
import com.sparta.ch4.delivery.company.domain.model.Product;
import com.sparta.ch4.delivery.company.domain.repository.CompanyRepository;
import com.sparta.ch4.delivery.company.domain.repository.ProductRepository;
import com.sparta.ch4.delivery.company.domain.type.ProductQuantity;
import com.sparta.ch4.delivery.company.domain.type.ProductSearchType;
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
public class ProductDomainService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    public Product createProduct(ProductDto productDto) {
        Company company = companyRepository.findById(productDto.companyId()).orElseThrow(
                () -> new EntityNotFoundException("ID 에 해당하는 업체를 찾을 수 없습니다.")
        );

        return productRepository.save(productDto.toEntity(company));
    }


    public Product getProductById(UUID productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("ID 에 해당하는 상품을 찾을 수 없습니다.")
        );
    }

    public Page<Product> getAllProducts(UUID companyId, UUID hubId, ProductSearchType searchType, String searchValue, Pageable pageable) {
        return productRepository.searchProducts(companyId, hubId, searchType, searchValue, pageable);
    }

    public Product updateProduct(UUID productId, ProductDto productDto) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("ID 에 해당하는 상품을 찾을 수 없습니다.")
        );

        // companyId 가 업데이트 될 때 존재하는 업체인지 확인
        if (!productDto.companyId().equals(product.getCompany().getId())) {
            Company company = companyRepository.findById(productDto.companyId()).orElseThrow(
                    () -> new EntityNotFoundException("ID 에 해당하는 업체를 찾을 수 없습니다.")
            );
            //있다면 업데이트
            product.setCompany(company);
        }
        //다른 필드 업데이트
        product.setName(productDto.name());
        product.setQuantity(productDto.quantity());
        product.setHubId(productDto.hubId());

        return productRepository.save(product);
    }

    public void deleteProduct(UUID productId, String userId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("ID 에 해당하는 상품을 찾을 수 없습니다.")
        );
        // Soft delete
        product.setDeletedAt(LocalDateTime.now());
        product.setDeletedBy(userId);
        product.setIsDeleted(true);
    }

    public void updateProductQuantity(UUID productId, Integer quantity, ProductQuantity upAndDown) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("ID 에 해당하는 상품을 찾을 수 없습니다.")
        );
        switch(upAndDown){
            case UP -> product.setQuantity(product.getQuantity() + quantity);
            case DOWN -> {
                // TODO : 커스텀 에러 정의
                if (product.getQuantity() < quantity) throw new IllegalArgumentException("재고가 부족합니다.");
                product.setQuantity(product.getQuantity() - quantity);
            }
        }
    }
}