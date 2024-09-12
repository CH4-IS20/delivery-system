package com.sparta.ch4.delivery.order.presentation.request;

import com.sparta.ch4.delivery.order.application.dto.OrderCreateDto;
import com.sparta.ch4.delivery.order.application.dto.OrderDto;
import com.sparta.ch4.delivery.order.domain.type.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderCreateRequest(
        @NotNull(message = "요청(공급) 업체 ID 는 비어있을 수 없습니다.")
        UUID supplierId,  // 요청(공급) 업체

        @NotNull(message = "수령 업체 ID 는 비어있을 수 없습니다.")
        UUID receiverId,     // 수령 업체 ID

        @NotNull(message = "상품 ID 는 비어있을 수 없습니다.")
        UUID productId,  // 상품 ID

        @NotNull(message = "주문 상품 수량은 비어있을 수 없습니다.")
        @Min(value = 0)
        Integer quantity,

        @NotBlank(message = "수령 업체 주소는 비어있을 수 없습니다.")
        String receiptAddress,

        @NotBlank(message = "수령인 이름은 비어있을 수 없습니다.")
        String recipientName,

        @NotBlank(message = "수령인 슬랙 아이디는 비어있을 수 없습니다.")
        String recipientSlack
) {


    public OrderCreateDto toDto(String userId) {
        return OrderCreateDto.builder()
                .userId(Long.parseLong(userId))
                .supplierId(supplierId)
                .receiverId(receiverId)
                .productId(productId)
                .quantity(quantity)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .createdBy(userId)
                .receiptAddress(receiptAddress)
                .recipientName(recipientName)
                .recipientSlack(recipientSlack)
                .build();
    }
}
