package com.sparta.ch4.delivery.order.domain.repository;

import com.sparta.ch4.delivery.order.domain.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID>, DeliveryRepositoryCustom {
}
