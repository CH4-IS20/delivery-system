package com.example.hub.infrastructure.repository;

import com.example.hub.application.dto.HubDto;
import com.example.hub.domain.model.Hub;
import com.example.hub.domain.model.QHub;
import com.example.hub.domain.repository.HubRepositoryCustom;
import com.example.hub.presentation.response.HubResponse;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QHub hub = QHub.hub;

    @Override
    public Page<HubResponse> searchHub(String searchValue, Pageable pageable) {
        // 정렬 기준 설정
        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

        QueryResults<Hub> results = queryFactory
                .selectFrom(hub)
                .where(
                        hubNameContains(searchValue)
                )
                // 정렬
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                // 페이징 처리
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                // 결과값 반환
                .fetchResults();

        List<HubResponse> content = results.getResults().stream()
                .map(h -> HubResponse.fromDto(HubDto.fromEntity(h)))
                .collect(Collectors.toList());
        long total = results.getTotal();

        // PageImpl 객체를 생성하여 결과를 반환합니다. 이 객체는 페이지 내용, 페이지 정보, 총 결과 수를 포함
        return new PageImpl<>(content, pageable, total);
    }


    // 허브 이름 검색
    private BooleanExpression hubNameContains(String hubName) {
        return hubName != null ? hub.name.contains(hubName) : null;
    }

    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        // pageable 객체에서 정렬 정보가 존재하는지 확인합니다. 만약 정렬정보가 존재한다면
        if (pageable.getSort() != null) {
            // pageable.getSort()로부터 반환된 Sort.Order 객체들을 반복
            for (Sort.Order sortOrder : pageable.getSort()) {

                com.querydsl.core.types.Order direction = sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                switch (sortOrder.getProperty()) {
                    case "createdAt":
                        orders.add(new OrderSpecifier<>(direction, hub.createdAt));
                        break;
                    case "updatedAt":
                        orders.add(new OrderSpecifier<>(direction, hub.updatedAt));
                        break;
                    default:
                        break;
                }
            }
        }

        return orders;
    }
}
