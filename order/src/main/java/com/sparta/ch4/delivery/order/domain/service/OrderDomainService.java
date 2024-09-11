package com.sparta.ch4.delivery.order.domain.service;


import com.sparta.ch4.delivery.order.application.dto.OrderDto;
import com.sparta.ch4.delivery.order.domain.model.Order;
import com.sparta.ch4.delivery.order.domain.repository.OrderRepository;
import com.sparta.ch4.delivery.order.domain.type.OrderSearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderDomainService {

    private final OrderRepository orderRepository;

    public Order create(Order order) {
        return orderRepository.save(order);
    }

    public Page<Order> getAll(OrderSearchType searchType, String searchValue, Pageable pageable) {
        return orderRepository.searchOrders(searchType, searchValue, pageable);
    }

//    public Order update(UUID orderId, Order updatedOrder) {
//        return orderRepository
//    }
}
