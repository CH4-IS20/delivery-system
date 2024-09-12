package com.sparta.ch4.delivery.order.domain.repository;

import com.sparta.ch4.delivery.order.domain.model.Delivery;
import com.sparta.ch4.delivery.order.domain.type.DeliverySearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DeliveryRepositoryCustom {
    Page<Delivery> searchDelivery(UUID startHubId, UUID endHubId, DeliverySearchType searchType, String searchValue, Pageable pageable);
}
