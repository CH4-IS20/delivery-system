package com.sparta.ch4.delivery.order.domain.repository;

import com.sparta.ch4.delivery.order.domain.model.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, UUID>, DeliveryHistoryRepositoryCustom {
}
