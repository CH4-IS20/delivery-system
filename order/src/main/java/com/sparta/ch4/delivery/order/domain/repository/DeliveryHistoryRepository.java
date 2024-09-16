package com.sparta.ch4.delivery.order.domain.repository;

import com.sparta.ch4.delivery.order.domain.model.DeliveryHistory;
import com.sparta.ch4.delivery.order.infrastructure.repository.DeliveryHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, UUID>, DeliveryHistoryRepositoryCustom {
    List<DeliveryHistory> findAllByDeliveryId(UUID deliveryId);
}
