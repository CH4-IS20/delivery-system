package com.sparta.ch4.delivery.company.presentation.response;

import com.sparta.ch4.delivery.company.application.dto.CompanyWithUserDto;
import com.sparta.ch4.delivery.company.domain.type.CompanyType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CompanyWithUserForOrderResponse(
        UUID id,
        UUID hubId,
        String name,
        CompanyType type,
        String address,
        String recipient,  // 주문 수령자
        UUID recipientSlack  // 주문 수령자 slackID
) {
    public static CompanyWithUserForOrderResponse from(CompanyWithUserDto companyAndUserForOrderDto) {
        return CompanyWithUserForOrderResponse.builder()
                .id(companyAndUserForOrderDto.id())
                .hubId(companyAndUserForOrderDto.hubId())
                .name(companyAndUserForOrderDto.name())
                .type(companyAndUserForOrderDto.type())
                .address(companyAndUserForOrderDto.address())
                .recipient(companyAndUserForOrderDto.recipient())
                .recipientSlack(companyAndUserForOrderDto.recipientSlack())
                .build();
    }
}
