package com.sparta.ch4.delivery.company.application.dto;

import com.sparta.ch4.delivery.company.domain.model.Company;
import com.sparta.ch4.delivery.company.domain.type.CompanyType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CompanyDto(
        UUID id,
        UUID hubId,
        String name,
        CompanyType type,
        String address,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy,
        boolean isDeleted
) {

    public static CompanyDto from(Company entity) {
        return CompanyDto.builder()
                .id(entity.getId())
                .hubId(entity.getHubId())
                .name(entity.getName())
                .type(entity.getType())
                .address(entity.getAddress())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .isDeleted(entity.getIsDeleted())
                .build();
    }

    public Company toEntity() {
        Company company =  Company.builder()
                .hubId(hubId)
                .name(name)
                .type(type)
                .address(address)
                .build();

        //createdBy 수동으로 넣어주기
        company.setCreatedBy(createdBy);
        return company;
    }
}
