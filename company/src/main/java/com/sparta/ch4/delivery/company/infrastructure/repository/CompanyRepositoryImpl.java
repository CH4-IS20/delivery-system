package com.sparta.ch4.delivery.company.infrastructure.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ch4.delivery.company.domain.model.Company;
import com.sparta.ch4.delivery.company.domain.model.QCompany;
import com.sparta.ch4.delivery.company.domain.repository.CompanyRepositoryCustom;
import com.sparta.ch4.delivery.company.domain.type.CompanySearchType;
import com.sparta.ch4.delivery.company.domain.type.CompanyType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QCompany company = QCompany.company;

    @Override
    public Page<Company> searchCompanies(CompanySearchType searchType, String searchValue, Pageable pageable) {

        List<Company> companies = queryFactory
                .selectFrom(company)
                .where(getSearchPredicate(searchType, searchValue))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(company.count())
                .from(company)
                .where(getSearchPredicate(searchType, searchValue))
                .fetchOne();

        assert total != null;

        return new PageImpl<>(companies, pageable, total);
    }

    // TODO : 업체 타입 검사가 더 필요할 수도 있음. Enum 에서 검증할지 고민
    private BooleanExpression getSearchPredicate(CompanySearchType searchType, String searchValue) {
        if (searchValue == null || searchType == null) {
            return null;
        }
        return switch (searchType) {
            case NAME -> company.name.containsIgnoreCase(searchValue);  // 업체 이름 검색
            case TYPE -> company.type.eq(CompanyType.valueOf(searchValue));  // 업체 타입 검색
            case ADDRESS -> company.address.containsIgnoreCase(searchValue);  // 업체 주소 검색
        };
    }

    //TODO : 삭제되어 비활성화 된 업체도 조회하는 지 논의
    private BooleanExpression isDeletedCondition() {
        return company.isDeleted.eq(false);
    }
}
