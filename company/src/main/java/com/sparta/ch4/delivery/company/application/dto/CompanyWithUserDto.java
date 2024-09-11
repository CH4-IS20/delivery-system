package com.sparta.ch4.delivery.company.application.dto;

import com.sparta.ch4.delivery.company.domain.type.CompanyType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CompanyWithUserDto(
        UUID id,
        UUID hubId,
        String name,
        CompanyType type,
        String recipient,  // 주문 수령자
        UUID recipientSlack,  // 주문 수령자 slackID
        String address
) {

    public static CompanyWithUserDto fromDtoAndUserInfo(CompanyDto companyDto, String recipient, UUID recipientSlack) {
        return CompanyWithUserDto.builder()
                .id(companyDto.id())
                .hubId(companyDto.hubId())
                .name(companyDto.name())
                .type(companyDto.type())
                .recipient(recipient)
                .recipientSlack(recipientSlack)
                .address(companyDto.address())
                .build();
    }
}
