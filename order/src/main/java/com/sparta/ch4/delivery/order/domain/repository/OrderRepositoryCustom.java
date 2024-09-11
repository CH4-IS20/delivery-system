package com.sparta.ch4.delivery.order.domain.repository;


import com.sparta.ch4.delivery.order.domain.model.Order;
import com.sparta.ch4.delivery.order.domain.type.OrderSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> searchOrders(OrderSearchType searchType, String searchValue, Pageable pageable);
}
