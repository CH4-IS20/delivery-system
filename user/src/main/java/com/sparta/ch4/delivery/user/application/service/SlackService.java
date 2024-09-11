package com.sparta.ch4.delivery.user.application.service;

import com.sparta.ch4.delivery.user.application.dto.SlackMessageDto;
import com.sparta.ch4.delivery.user.domain.service.SlackDomainService;
import com.sparta.ch4.delivery.user.domain.type.SlackSearchType;
import com.sparta.ch4.delivery.user.presentation.response.SlackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SlackService {

    private final SlackDomainService slackDomainService;

    @Transactional
    public SlackResponse saveSlackMessage(SlackMessageDto dto) {
        return SlackResponse.fromSlackMessageDto(slackDomainService.saveSlackMessage(dto));
    }

    @Transactional(readOnly = true)
    public Page<SlackResponse> getSlackMessages(
            SlackSearchType searchType, String searchValue, Pageable pageable) {
        return slackDomainService.getSlackMessages(searchType, searchValue, pageable).map(SlackResponse::fromSlackMessageDto);
    }

}
