package com.sparta.ch4.delivery.user.application.dto;

import com.sparta.ch4.delivery.user.domain.model.Slack;
import com.sparta.ch4.delivery.user.domain.model.User;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record SlackMessageDto (
        Long userId, // 사용자 DB ID
        String userSlackId,      // 사용자가 사용하는 슬랙 ID
        UUID slackMessageId, // 슬랙 고유 ID
        String message,    // 수신한 메시지 내용
        LocalDateTime receivedAt // 수신한 시간
)
{
    public Slack toEntity(User user) {
        Slack slack = Slack.builder()
                .slackId(userSlackId)
                .user(user)
                .message(message)
                .build();
        slack.setCreatedBy(String.valueOf(userId));
        return slack;
    }

    public static SlackMessageDto fromEntity(Slack entity) {
        return SlackMessageDto.builder()
                .slackMessageId(entity.getId())
                .userId(entity.getUser().getId())
                .message(entity.getMessage())
                .userSlackId(entity.getSlackId())
                .receivedAt(entity.getCreatedAt())
                .build();
    }
}
