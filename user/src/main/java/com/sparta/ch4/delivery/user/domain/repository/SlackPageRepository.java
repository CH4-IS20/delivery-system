package com.sparta.ch4.delivery.user.domain.repository;

import com.sparta.ch4.delivery.user.application.dto.SlackMessageDto;
import com.sparta.ch4.delivery.user.domain.type.SlackSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface SlackPageRepository {
    Page<SlackMessageDto> searchMessages(SlackSearchType searchType, String searchValue, Pageable pageable);
}
