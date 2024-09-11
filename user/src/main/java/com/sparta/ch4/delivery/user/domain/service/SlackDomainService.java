package com.sparta.ch4.delivery.user.domain.service;

import com.sparta.ch4.delivery.user.application.dto.SlackMessageDto;
import com.sparta.ch4.delivery.user.domain.model.Slack;
import com.sparta.ch4.delivery.user.domain.model.User;
import com.sparta.ch4.delivery.user.domain.repository.SlackPageRepository;
import com.sparta.ch4.delivery.user.domain.repository.SlackRepository;
import com.sparta.ch4.delivery.user.domain.repository.UserRepository;
import com.sparta.ch4.delivery.user.domain.type.SlackSearchType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackDomainService {

    private final UserRepository userRepository;

    private final SlackRepository slackRepository;

    private final SlackPageRepository slackPageRepository;

    public SlackMessageDto saveSlackMessage(SlackMessageDto dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("ID 에 해당하는 사용자를 찾을 수 없습니다."));
        Slack slack = slackRepository.save(dto.toEntity(user));
        return SlackMessageDto.fromEntity(slack);
    }

    public Page<SlackMessageDto> getSlackMessages(SlackSearchType searchType, String searchValue,
            Pageable pageable) {
        return slackPageRepository.searchMessages(searchType, searchValue, pageable);
    }


}
