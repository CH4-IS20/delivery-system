package com.sparta.ch4.delivery.user.domain.service;

import com.sparta.ch4.delivery.user.application.dto.SlackMessageDto;
import com.sparta.ch4.delivery.user.application.dto.SlackSendMessageDto;
import com.sparta.ch4.delivery.user.domain.model.Slack;
import com.sparta.ch4.delivery.user.domain.repository.SlackPageRepository;
import com.sparta.ch4.delivery.user.domain.repository.SlackRepository;
import com.sparta.ch4.delivery.user.domain.type.SlackSearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackDomainService {

    private final SlackRepository slackRepository;

    private final SlackPageRepository slackPageRepository;

    public SlackMessageDto saveSlackMessage(SlackSendMessageDto dto) {
        return SlackMessageDto.fromEntity(slackRepository.save(dto.toEntity()));
    }

    public Page<SlackMessageDto> getSlackMessages(SlackSearchType searchType, String searchValue,
            Pageable pageable) {
        return slackPageRepository.searchMessages(searchType, searchValue, pageable);
    }


}
