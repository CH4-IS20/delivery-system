package com.sparta.ch4.delivery.order.application.service;


import com.sparta.ch4.delivery.order.application.dto.DeliveryDto;
import com.sparta.ch4.delivery.order.application.dto.DeliveryStatusUpdateDto;
import com.sparta.ch4.delivery.order.domain.service.DeliveryDomainService;
import com.sparta.ch4.delivery.order.domain.service.OrderDomainService;
import com.sparta.ch4.delivery.order.domain.type.DeliverySearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeliveryService {
    private final DeliveryDomainService deliveryDomainService;

    // status  업데이트
    @Transactional
    public DeliveryDto updateDelivery(UUID deliveryId, DeliveryStatusUpdateDto statusUpdateDto){
        return DeliveryDto.from(deliveryDomainService.updateStatus(deliveryId, statusUpdateDto));
    }

    @Transactional(readOnly = true)
    public Page<DeliveryDto> getAllDelivery(
            UUID startHubId, UUID endHubId,
            DeliverySearchType searchType, String searchValue, Pageable pageable){
        return deliveryDomainService.getAll(startHubId, endHubId, searchType, searchValue, pageable).map(DeliveryDto::from);
    }

    @Transactional(readOnly = true)
    public DeliveryDto getDelivery(UUID deliveryId){
        return DeliveryDto.from(deliveryDomainService.getOne(deliveryId));
    }

    @Transactional
    public void deleteDelivery(UUID deliveryId, String deletedBy){
        deliveryDomainService.delete(deliveryId, deletedBy);
    }

}
