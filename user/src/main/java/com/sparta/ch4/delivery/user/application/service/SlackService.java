package com.sparta.ch4.delivery.user.application.service;

import com.sparta.ch4.delivery.user.application.dto.SlackSendMessageDto;
import com.sparta.ch4.delivery.user.domain.service.SlackDomainService;
import com.sparta.ch4.delivery.user.domain.type.SlackSearchType;
import com.sparta.ch4.delivery.user.presentation.response.SlackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SlackService {

    private final SlackDomainService slackDomainService;

    @Value("${slack.webhook-url}")
    private String webhookUrl;

    @Transactional
    public SlackResponse sendSlackMessage(SlackSendMessageDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String payload = getPayload(dto.receiverSlackId(), dto.message());
        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(webhookUrl, HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            throw new RestClientException("Slack 메시지 전송 중 에러가 발생했습니다.");
        }
        return saveSlackMessage(dto);
    }

    public SlackResponse saveSlackMessage(SlackSendMessageDto dto) {
        return SlackResponse.fromSlackMessageDto(slackDomainService.saveSlackMessage(dto));
    }

    @Transactional(readOnly = true)
    public Page<SlackResponse> getSlackMessages(
            SlackSearchType searchType, String searchValue, Pageable pageable) {
        return slackDomainService.getSlackMessages(searchType, searchValue, pageable).map(SlackResponse::fromSlackMessageDto);
    }

    private String getPayload(String recipientSlackId, String message) {
        return String.format("""
                {
                    "channel": "@%s",
                    "username": "IS20bot",
                    "text": "%s"
                }
                """, recipientSlackId, message);
    }

}
