package com.sparta.ch4.delivery.user.presentation.response;

import com.sparta.ch4.delivery.user.application.dto.SlackMessageDto;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record SlackResponse(
        Long userId,
        String userSlackId,
        UUID slackMessageId,
        String message,
        LocalDateTime receivedAt
) {
    public static SlackResponse fromSlackMessageDto(SlackMessageDto dto) {
        return SlackResponse.builder()
                .userId(dto.userId())
                .userSlackId(dto.userSlackId())
                .slackMessageId(dto.slackMessageId())
                .message(dto.message())
                .receivedAt(dto.receivedAt())
                .build();
    }
}
