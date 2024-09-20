package com.sparta.ch4.delivery.order.presentation.request;

import com.sparta.ch4.delivery.order.application.dto.DeliveryHistoryDto;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/*
  배송자의 다른 서버 시스템에서 현재 실제 거리와 실제 소요 시간을 계산하고 배송현황이 업데이트 될 때 같이 수정하는 것으로 상정함.
 */
@Schema(description = "배달 기록 현황 수정 요청")
public record DeliveryHistoryStatusUpdateRequest(
        @NotNull(message = "실제 거리는 비어있을 수 없습니다.")
        Float actualDistance,
        @NotNull(message = "실제 소요 시간은 비어있을 수 없습니다.")
        Integer actualDuration,

        @NotNull(message = "배송현황(상태)는 비어있을 수 없습니다.")
        @Schema(description = "이동 상태", enumAsRef = true)
        DeliveryStatus status
) {

    public DeliveryHistoryDto toDto(String userIdForUpdatedBy){
        return DeliveryHistoryDto.builder()
                .actualDistance(actualDistance)
                .actualDuration(actualDuration)
                .status(status)
                .updatedBy(userIdForUpdatedBy)
                .build();
    }
}
