package com.example.hub.application.service;

import com.example.hub.domain.service.DeliveryManagerDomainService;
import com.example.hub.presentation.request.DeliveryManagerCreateRequest;
import com.example.hub.presentation.response.DeliveryManagerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

    private final DeliveryManagerDomainService deliveryManagerDomainService;

    @Transactional
    public DeliveryManagerResponse createDeliveryManager(DeliveryManagerCreateRequest request,String userId) {
        return deliveryManagerDomainService.createDeliveryManager(request,userId);
    }

    @Transactional(readOnly = true)
    public DeliveryManagerResponse readDeliveryManager(UUID deliveryManagerId, String userId,String role) {
        return deliveryManagerDomainService.readDeliveryManager(deliveryManagerId, userId, role);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryManagerResponse> searchDeliveryManager(String searchValue, Pageable pageable) {
        return deliveryManagerDomainService.searchDeliveryManager(searchValue,pageable);
    }

    @Transactional
    public DeliveryManagerResponse updateDeliveryManager(DeliveryManagerCreateRequest request, UUID deliveryManagerId, String userId, String role) {
        return deliveryManagerDomainService.updateDeliveryManager(request,deliveryManagerId,userId,role);
    }

    @Transactional
    public void deleteDeliveryManager(UUID deliveryManagerId, String userId, String role) {
        deliveryManagerDomainService.deleteDeliveryManager(deliveryManagerId,userId,role);
    }
}
