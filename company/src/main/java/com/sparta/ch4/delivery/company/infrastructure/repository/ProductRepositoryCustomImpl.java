package com.sparta.ch4.delivery.company.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ch4.delivery.company.domain.repository.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}