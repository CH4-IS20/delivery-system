package com.sparta.ch4.delivery.order.domain.service;


import com.sparta.ch4.delivery.order.application.dto.OrderDto;
import com.sparta.ch4.delivery.order.domain.model.Order;
import com.sparta.ch4.delivery.order.domain.repository.OrderRepository;
import com.sparta.ch4.delivery.order.domain.type.OrderSearchType;
import com.sparta.ch4.delivery.order.domain.type.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found By Id"));
    }

    public Order update(Order updatingOrder, OrderDto dto) {
        updatingOrder.setQuantity(dto.quantity());
        updatingOrder.setUpdatedBy(dto.updatedBy());
        return orderRepository.save(updatingOrder);
    }

    public void delete(UUID orderId, String deletedBy) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.CANCELED);
        order.setDeletedAt(LocalDateTime.now());
        order.setDeletedBy(deletedBy);
        order.setIsDeleted(true);
    }
}
