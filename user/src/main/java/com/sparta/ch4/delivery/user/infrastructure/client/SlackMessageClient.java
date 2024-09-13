package com.sparta.ch4.delivery.user.infrastructure.client;

import com.sparta.ch4.delivery.user.application.dto.SlackSendMessageDto;
import com.sparta.ch4.delivery.user.application.service.SlackService;
import com.sparta.ch4.delivery.user.domain.exception.ApplicationException;
import com.sparta.ch4.delivery.user.domain.exception.ErrorCode;
import com.sparta.ch4.delivery.user.presentation.response.SlackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SlackMessageClient {

    private final SlackService slackService;

    @Value("${slack.webhook-url}")
    private String webhookUrl;

    @Transactional
    public SlackResponse sendSlackMessage(SlackSendMessageDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String payload = createSlackPayload(dto.receiverSlackId(), dto.message());
        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(webhookUrl, HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            throw new ApplicationException(ErrorCode.SLACK_SEND_MESSAGE_FAIL);
        }
        return slackService.saveSlackMessage(dto);
    }

    public String createSlackPayload(String recipientSlackId, String message) {
        return String.format("""
                {
                    "channel": "@%s",
                    "username": "IS20bot",
                    "text": "%s"
                }
                """, recipientSlackId, message);
    }

}
