package com.sparta.ch4.delivery.user.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ch4.delivery.user.application.dto.UserPageDto;
import com.sparta.ch4.delivery.user.domain.model.QUser;
import com.sparta.ch4.delivery.user.domain.repository.UserPageRepository;
import com.sparta.ch4.delivery.user.domain.type.UserSearchType;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserPageRepositoryImpl implements UserPageRepository {

    private final JPAQueryFactory queryFactory;

    QUser user = QUser.user;

    // TODO: 삭제된 레코드 조회 제외?
    @Override
    public Page<UserPageDto> searchUser(UserSearchType searchType, String searchValue, Pageable pageable) {

        List<UserPageDto> results = queryFactory
                .select(Projections.constructor(
                        UserPageDto.class,
                        user.id.as("id"),
                        user.hubId.as("hubId"),
                        user.companyId.as("companyId"),
                        user.username,
                        user.email,
                        user.role,
                        user.createdAt
                ))
                .from(user)
                .where(getSearchPredicate(searchType, searchValue))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(user.count())
                .from(user)
                .where(getSearchPredicate(searchType, searchValue))
                .fetchOne();

        assert total != null;

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression getSearchPredicate(UserSearchType searchType, String searchValue) {
        if (searchType == null || searchValue == null) {
            return null;
        }
        return switch (searchType) {
            case USERNAME -> user.username.containsIgnoreCase(searchValue);
            case EMAIL -> user.email.eq(searchValue);
            case HUB_ID -> user.hubId.eq(UUID.fromString(searchValue));
            case COMPANY_ID ->
                    user.companyId.eq(UUID.fromString(searchValue));
        };
    }
}
