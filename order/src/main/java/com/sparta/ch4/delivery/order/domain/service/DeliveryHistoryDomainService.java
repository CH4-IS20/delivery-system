package com.sparta.ch4.delivery.order.domain.service;


import com.sparta.ch4.delivery.order.domain.model.DeliveryHistory;
import com.sparta.ch4.delivery.order.domain.repository.DeliveryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DeliveryHistoryDomainService {
    private final DeliveryHistoryRepository deliveryHistoryRepository;

    public List<DeliveryHistory> create(List<DeliveryHistory> deliveryHistory) {
        return deliveryHistoryRepository.saveAll(deliveryHistory);
    }

}
