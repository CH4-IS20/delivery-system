package com.sparta.ch4.delivery.order.domain.repository;


import com.sparta.ch4.delivery.order.domain.model.Order;
import com.sparta.ch4.delivery.order.infrastructure.repository.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository  extends JpaRepository<Order, UUID>, OrderRepositoryCustom {
}
