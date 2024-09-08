package com.sparta.ch4.delivery.company.application.dto;

import com.sparta.ch4.delivery.company.domain.model.Company;
import com.sparta.ch4.delivery.company.domain.model.Product;
import com.sparta.ch4.delivery.company.domain.type.CompanyType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ProductDto(
        UUID id,
        UUID companyId,
        String companyName,
        CompanyType companyType,
        String companyAddress,
        UUID hubId,
        String name,
        Integer quantity,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy,
        Boolean isDeleted
) {

    public static ProductDto from(Product entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .companyId(entity.getCompany().getId())
                .companyName(entity.getCompany().getName())
                .companyType(entity.getCompany().getType())
                .companyAddress(entity.getCompany().getAddress())
                .hubId(entity.getHubId())
                .name(entity.getName())
                .quantity(entity.getQuantity())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .isDeleted(entity.getIsDeleted())
                .build();
    }

    public Product toEntity(Company company) {
        Product product =  Product.builder()
                .id(id)
                .company(company)
                .hubId(hubId)
                .name(name)
                .quantity(quantity)
                .build();

        product.setCreatedBy(createdBy);
        return product;
    }
}
