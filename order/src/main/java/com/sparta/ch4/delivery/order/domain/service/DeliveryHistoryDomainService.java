package com.sparta.ch4.delivery.order.domain.service;


import com.sparta.ch4.delivery.order.application.dto.DeliveryHistoryDto;
import com.sparta.ch4.delivery.order.domain.model.Delivery;
import com.sparta.ch4.delivery.order.domain.model.DeliveryHistory;
import com.sparta.ch4.delivery.order.domain.repository.DeliveryHistoryRepository;
import com.sparta.ch4.delivery.order.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeliveryHistoryDomainService {
    private final DeliveryHistoryRepository deliveryHistoryRepository;
    private final DeliveryRepository deliveryRepository;

    public List<DeliveryHistory> create(List<DeliveryHistory> deliveryHistory) {
        return deliveryHistoryRepository.saveAll(deliveryHistory);
    }

    public DeliveryHistory getOne(UUID deliveryHistoryId) {
        return deliveryHistoryRepository.findById(deliveryHistoryId).orElseThrow(
                () -> new IllegalArgumentException("ID 에 해당하는 배송 기록을 찾을 수 없습니다.")
        );
    }

    public List<DeliveryHistory> getDeliveryHistoriesByDeliveryId(UUID deliveryId) {
        return deliveryHistoryRepository.findAllByDeliveryId(deliveryId);
    }

    public DeliveryHistory updateStatus(UUID deliveryHistoryId, DeliveryHistoryDto updatingDto) {
        DeliveryHistory deliveryHistory = getOne(deliveryHistoryId);
        deliveryHistory.setActualDistance(updatingDto.actualDistance());
        deliveryHistory.setActualDuration(updatingDto.actualDuration());
        deliveryHistory.setStatus(updatingDto.status());
        deliveryHistory.setUpdatedBy(updatingDto.updatedBy());
        return deliveryHistoryRepository.save(deliveryHistory);
    }

    public void deleteAll(UUID deliveryId, String userIdForDeletedBy) {
        if (deliveryRepository.existsByIdAndIsDeletedFalse(deliveryId)) { // 삭제되지 않은 배송이라면 에러 반환
            throw new RuntimeException("삭제되지 않은 배송의 기록은 삭제할 수 없습니다.");
        }
        List<DeliveryHistory> deliveryHistoryList = deliveryHistoryRepository.findAllByDeliveryId(deliveryId);
        deliveryHistoryList.forEach( history -> {
            history.setIsDeleted(true);
            history.setDeletedAt(LocalDateTime.now());
            history.setDeletedBy(userIdForDeletedBy);
        });
        //bulk delete
        deliveryHistoryRepository.deleteAll(deliveryHistoryList);
    }
}
