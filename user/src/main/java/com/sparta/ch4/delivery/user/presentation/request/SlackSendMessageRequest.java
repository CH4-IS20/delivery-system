package com.sparta.ch4.delivery.user.presentation.request;

import com.sparta.ch4.delivery.user.application.dto.SlackSendMessageDto;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "슬랙 메시지 전송 요청 데이터")
public record SlackSendMessageRequest (

        @NotBlank(message = "사용자 Slack 아이디는 필수입니다.")
        @Schema(description = "메시지를 받을 사용자의 Slack ID", example = "U12345678")
        String receiverSlackId,

        @NotBlank(message = "메시지 내용은 필수입니다.")
        @Schema(description = "전송할 메시지 내용", example = "안녕하세요, 새로운 공지사항이 있습니다.")
        String message
) {
    public SlackSendMessageDto toDto() {
        return SlackSendMessageDto.builder()
                .receiverSlackId(receiverSlackId)
                .message(message)
                .build();
    }
}