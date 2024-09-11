package com.sparta.ch4.delivery.user.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ch4.delivery.user.application.dto.SlackMessageDto;
import com.sparta.ch4.delivery.user.domain.model.QSlack;
import com.sparta.ch4.delivery.user.domain.repository.SlackPageRepository;
import com.sparta.ch4.delivery.user.domain.type.SlackSearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SlackPageRepositoryImpl implements SlackPageRepository {

    private final JPAQueryFactory queryFactory;

    QSlack slack = QSlack.slack;

    public Page<SlackMessageDto> searchMessages(SlackSearchType searchType, String searchValue, Pageable pageable) {

        List<SlackMessageDto> results = queryFactory
                .select(Projections.constructor(
                        SlackMessageDto.class,
                        slack.user.id.as("userId"),
                        slack.slackId,
                        slack.id.as("slackMessageId"),
                        slack.message,
                        slack.createdAt.as("sentAt")
                ))
                .from(slack)
                .where(getSearchPredicate(searchType, searchValue))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(slack.count())
                .from(slack)
                .where(getSearchPredicate(searchType, searchValue))
                .fetchOne();

        assert total != null;

        return new PageImpl<>(results, pageable, total);
    }

    // 검색 조건 생성
    private BooleanExpression getSearchPredicate(SlackSearchType searchType, String searchValue) {

        if (searchType == null || searchValue == null) {
            return null;
        }

        return switch (searchType) {
            case SLACK_ID -> slack.slackId.containsIgnoreCase(searchValue);
            case MESSAGE_CONTENT -> slack.message.containsIgnoreCase(searchValue);
        };
    }
}

