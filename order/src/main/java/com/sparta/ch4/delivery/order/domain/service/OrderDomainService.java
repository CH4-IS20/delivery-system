package com.sparta.ch4.delivery.order.domain.service;


import com.sparta.ch4.delivery.order.domain.model.Order;
import com.sparta.ch4.delivery.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderDomainService {

    private final OrderRepository orderRepository;

    public Order create(Order order) {
        return orderRepository.save(order);
    }
}
