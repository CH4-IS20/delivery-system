package com.sparta.ch4.delivery.company.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ch4.delivery.company.domain.repository.CompanyRepositoryCustom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}
