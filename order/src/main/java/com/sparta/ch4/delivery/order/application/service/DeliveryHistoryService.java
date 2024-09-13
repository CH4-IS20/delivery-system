package com.sparta.ch4.delivery.order.application.service;


import com.sparta.ch4.delivery.order.application.dto.DeliveryHistoryDto;
import com.sparta.ch4.delivery.order.domain.service.DeliveryHistoryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DeliveryHistoryService {

    private final DeliveryHistoryDomainService deliveryHistoryDomainService;

    @Transactional
    public DeliveryHistoryDto getDelivery(UUID deliveryHistoryId){
        return DeliveryHistoryDto.from(deliveryHistoryDomainService.getOne(deliveryHistoryId));
    }

    @Transactional
    public List<DeliveryHistoryDto> getDeliveryHistoriesForDelivery(UUID deliveryId){
        return deliveryHistoryDomainService.getDeliveryHistoriesByDeliveryId(deliveryId).stream()
                .map(DeliveryHistoryDto::from).collect(Collectors.toList());
    }

    @Transactional
    public DeliveryHistoryDto updateDeliveryHistoryForRealEstimate(UUID deliveryHistoryId, DeliveryHistoryDto updatingDto){
        return DeliveryHistoryDto.from(deliveryHistoryDomainService.updateStatus(deliveryHistoryId, updatingDto));
    }

    @Transactional
    public void deleteAllDeliveryHistory(UUID deliveryId, String userIdForDeletedBy){
        deliveryHistoryDomainService.deleteAll(deliveryId,userIdForDeletedBy);
    }
}
