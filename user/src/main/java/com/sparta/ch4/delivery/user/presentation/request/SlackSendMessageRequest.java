package com.sparta.ch4.delivery.user.presentation.request;

import com.sparta.ch4.delivery.user.application.dto.SlackMessageDto;
import jakarta.validation.constraints.NotBlank;

public record SlackSendMessageRequest (
        @NotBlank(message = "사용자 Slack 아이디는 필수입니다.")
        String userSlackId,

        @NotBlank(message = "메시지는 내용은 필수입니다.")
        String message
) {
    public SlackMessageDto toDto(String userId) {
        return SlackMessageDto.builder()
                .userSlackId(userSlackId)
                .userId(Long.valueOf(userId))
                .message(message)
                .build();
    }
}

