package com.sparta.ch4.delivery.user.presentation.response;

import com.sparta.ch4.delivery.user.application.dto.SlackMessageDto;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record SlackResponse(
        String receiverSlackId,
        UUID slackMessageId,
        String message,
        LocalDateTime receivedAt
) {
    public static SlackResponse fromSlackMessageDto(SlackMessageDto dto) {
        return SlackResponse.builder()
                .receiverSlackId(dto.receiverSlackId())
                .slackMessageId(dto.slackMessageId())
                .message(dto.message())
                .receivedAt(dto.receivedAt())
                .build();
    }
}
