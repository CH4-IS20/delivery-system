package com.sparta.ch4.delivery.order.infrastructure.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ch4.delivery.order.domain.model.Order;
import com.sparta.ch4.delivery.order.domain.model.QOrder;
import com.sparta.ch4.delivery.order.domain.repository.OrderRepositoryCustom;
import com.sparta.ch4.delivery.order.domain.type.OrderSearchType;
import com.sparta.ch4.delivery.order.domain.type.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QOrder order = QOrder.order;

    @Override
    public Page<Order> searchOrders(OrderSearchType searchType, String searchValue, Pageable pageable) {
        List<Order> orders = queryFactory
                .selectFrom(order)
                .where(getSearchPredicate(searchType, searchValue))
                .orderBy(getSortOrder(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(order.count())
                .from(order)
                .where(getSearchPredicate(searchType, searchValue))
                .fetchOne();

        assert total != null;

        return new PageImpl<>(orders, pageable, total);
    }

    private BooleanExpression getSearchPredicate(OrderSearchType searchType, String searchValue) {
        if (searchValue == null || searchType == null) {
            return null;
        }
        return switch (searchType) {
            case USER_ID -> order.userId.eq(Long.valueOf(searchValue));
            case SUPPLIER_ID -> order.supplierId.eq(UUID.fromString(searchValue));
            case RECEIVER_ID -> order.receiverId.eq(UUID.fromString(searchValue));
            case PRODUCT_ID -> order.productId.eq(UUID.fromString(searchValue));
            case QUANTITY -> order.quantity.eq(Integer.parseInt(searchValue));
            case ORDER_DATE -> order.orderDate.eq(LocalDateTime.parse(searchValue));
            case STATUS -> order.status.eq(OrderStatus.valueOf(searchValue.toUpperCase()));
        };
    }

    // isDeleted = true 인 것도 조회할 수 있으니 전체 쿼리에선 아직 사용하지 않음
    private BooleanExpression isDeletedCondition() {
        return order.isDeleted.eq(false);
    }

    private OrderSpecifier<?>[] getSortOrder(Sort sort) {
        return sort.stream().map(order -> {
            String property = order.getProperty();
            boolean isAscending = order.isAscending();

            return switch (property) {
                case "createdAt" -> isAscending ? this.order.createdAt.asc() : this.order.createdAt.desc();
                case "updatedAt" -> isAscending ? this.order.updatedAt.asc() : this.order.updatedAt.desc();
                case "quantity" -> isAscending ? this.order.quantity.asc() : this.order.quantity.desc();
                default -> null;
            };
        }).filter(Objects::nonNull).toArray(OrderSpecifier[]::new);
    }
}
