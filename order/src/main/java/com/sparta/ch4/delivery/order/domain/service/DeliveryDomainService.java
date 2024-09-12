package com.sparta.ch4.delivery.order.domain.service;


import com.sparta.ch4.delivery.order.application.dto.DeliveryDto;
import com.sparta.ch4.delivery.order.application.dto.DeliveryStatusUpdateDto;
import com.sparta.ch4.delivery.order.domain.model.Delivery;
import com.sparta.ch4.delivery.order.domain.repository.DeliveryRepository;
import com.sparta.ch4.delivery.order.domain.type.DeliverySearchType;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeliveryDomainService {

    private final DeliveryRepository deliveryRepository;

    public Delivery create(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    public Page<Delivery> getAll(UUID startHubId, UUID endHubId, DeliverySearchType searchType, String searchValue, Pageable pageable) {
        return deliveryRepository.searchDelivery(startHubId, endHubId, searchType, searchValue, pageable);
    }

    public Delivery getOne(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow(
                ()-> new IllegalArgumentException("ID 에 해당하는 배송 정보가 없습니다.")
        );
    }

    public void delete(UUID deliveryId, String deletedBy) {
        Delivery delivery = getOne(deliveryId);
        delivery.setStatus(DeliveryStatus.HUB_WAITING);
        delivery.setDeletedBy(deletedBy);
        delivery.setDeletedAt(LocalDateTime.now());
        delivery.setIsDeleted(true);
    }

    public Delivery updateStatus(UUID deliveryId, DeliveryStatusUpdateDto statusUpdateDto) {
        Delivery delivery = getOne(deliveryId);
        delivery.setStatus(statusUpdateDto.status());
        delivery.setUpdatedBy(statusUpdateDto.updatedBy());
        return deliveryRepository.save(delivery);
    }
}
