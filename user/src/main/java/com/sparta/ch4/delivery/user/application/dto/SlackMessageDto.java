package com.sparta.ch4.delivery.user.application.dto;

import com.sparta.ch4.delivery.user.domain.model.Slack;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record SlackMessageDto (
        String receiverSlackId,      // 수신자의 슬랙 아이디
        UUID slackMessageId, // 슬랙 레코드 ID
        String message,    // 수신한 메시지 내용
        LocalDateTime receivedAt // 수신한 시간
)
{
    public static SlackMessageDto fromEntity(Slack entity) {
        return SlackMessageDto.builder()
                .receiverSlackId(entity.getReceiverSlackId())
                .slackMessageId(entity.getId())
                .message(entity.getMessage())
                .receivedAt(entity.getCreatedAt())
                .build();
    }
}
