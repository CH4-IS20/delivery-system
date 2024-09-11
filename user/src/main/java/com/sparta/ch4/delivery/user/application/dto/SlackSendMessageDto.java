package com.sparta.ch4.delivery.user.application.dto;

import com.sparta.ch4.delivery.user.domain.model.Slack;
import lombok.Builder;

@Builder
public record SlackSendMessageDto(
        String receiverSlackId,
        String message
)
{
    public Slack toEntity() {
        return Slack.builder()
                .receiverSlackId(receiverSlackId)
                .message(message)
                .build();
    }
}
