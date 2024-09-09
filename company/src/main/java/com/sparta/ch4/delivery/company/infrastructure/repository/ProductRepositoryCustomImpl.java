package com.sparta.ch4.delivery.company.infrastructure.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ch4.delivery.company.domain.model.Product;
import com.sparta.ch4.delivery.company.domain.model.QCompany;
import com.sparta.ch4.delivery.company.domain.model.QProduct;
import com.sparta.ch4.delivery.company.domain.repository.ProductRepositoryCustom;
import com.sparta.ch4.delivery.company.domain.type.ProductSearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QProduct product = QProduct.product;
    QCompany company = QCompany.company;

    @Override
    public Page<Product> searchProducts(UUID companyId, UUID hubId, ProductSearchType searchType, String searchValue, Pageable pageable) {
        List<Product> products = queryFactory
                .selectFrom(product).distinct()
                .leftJoin(product.company, company) // 상품은 연관된 업체와 연관지어 검색
                .where(
                        getSearchPredicate(searchType, searchValue)
                        , filterByCompanyId(companyId)
                        , filterByHubId(hubId)
                        , isDeletedCondition()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(product.count())
                .from(product).distinct()
                .leftJoin(product.company, company)
                .where(
                        getSearchPredicate(searchType, searchValue)
                        , filterByCompanyId(companyId)
                        , filterByHubId(hubId)
                        , isDeletedCondition()
                )
                .fetchOne();

        assert total != null;

        return new PageImpl<>(products, pageable, total);
    }


    private BooleanExpression getSearchPredicate(ProductSearchType searchType, String searchValue) {
        if (searchValue == null || searchType == null) {
            return null;
        }
        return switch (searchType) {
            case NAME -> product.name.containsIgnoreCase(searchValue);  // 상품 이름 검색
            case COMPANY_NAME -> product.company.name.containsIgnoreCase(searchValue);  // 회사 이름 검색
        };
    }

    private BooleanExpression isDeletedCondition() {
        return product.isDeleted.eq(false);
    }

    // 관리 업체 필터링
    private BooleanExpression filterByCompanyId(UUID companyId) {
        return companyId != null ? product.company.id.eq(companyId) : null;
    }

    // 관리 허브 필터링
    private BooleanExpression filterByHubId(UUID hubId) {
        return hubId != null ? product.hubId.eq(hubId) : null;
    }

}