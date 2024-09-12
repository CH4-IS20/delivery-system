package com.sparta.ch4.delivery.order.infrastructure.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ch4.delivery.order.domain.model.Delivery;
import com.sparta.ch4.delivery.order.domain.model.QDelivery;
import com.sparta.ch4.delivery.order.domain.repository.DeliveryRepositoryCustom;
import com.sparta.ch4.delivery.order.domain.type.DeliverySearchType;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import com.sparta.ch4.delivery.order.domain.type.OrderSearchType;
import com.sparta.ch4.delivery.order.domain.type.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class DeliveryRepositoryCustomImpl implements DeliveryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QDelivery delivery = QDelivery.delivery;

    @Override
    public Page<Delivery> searchDelivery(UUID startHubId, UUID endHubId, DeliverySearchType searchType, String searchValue, Pageable pageable) {
        BooleanExpression isStartHubIdCondition = startHubId != null ? delivery.startHub.eq(startHubId) : null;
        BooleanExpression isEndHubIdCondition = endHubId != null ? delivery.endHub.eq(endHubId) : null;

        List<Delivery> deliveries = queryFactory.selectFrom(delivery)
                .where(getSearchPredicate(searchType, searchValue), isStartHubIdCondition, isEndHubIdCondition)
                .orderBy(getSortOrder(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(delivery.count()).from(delivery)
                .where()
                .fetchOne();
        assert total != null;

        return new PageImpl<>(deliveries, pageable, total);
    }

    private BooleanExpression getSearchPredicate(DeliverySearchType searchType, String searchValue) {
        if (searchValue == null || searchType == null) {
            return null;
        }
        return switch (searchType) {
            case STATUS -> delivery.status.eq(DeliveryStatus.valueOf(searchValue.toUpperCase()));
            case DELIVERY_ADDRESS -> delivery.deliveryAddress.containsIgnoreCase(searchValue);
            case RECIPIENT_NAME ->  delivery.recipient.containsIgnoreCase(searchValue);
        };
    }

    // isDeleted = true 인 것도 조회할 수 있으니 전체 쿼리에선 아직 사용하지 않음
    private BooleanExpression isDeletedCondition() {
        return delivery.isDeleted.eq(false);
    }

    private OrderSpecifier<?>[] getSortOrder(Sort sort) {
        return sort.stream().map(delivery -> {
            String property = delivery.getProperty();
            boolean isAscending = delivery.isAscending();

            return switch (property) {
                case "createdAt" -> isAscending ? this.delivery.createdAt.asc() : this.delivery.createdAt.desc();
                case "updatedAt" -> isAscending ? this.delivery.updatedAt.asc() : this.delivery.updatedAt.desc();
                case "status" -> isAscending ? this.delivery.status.asc() : this.delivery.status.desc();
                default -> null;
            };
        }).filter(Objects::nonNull).toArray(OrderSpecifier[]::new);
    }
}
