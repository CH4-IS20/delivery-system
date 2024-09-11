package com.sparta.ch4.delivery.order.domain.service;


import com.sparta.ch4.delivery.order.domain.model.Delivery;
import com.sparta.ch4.delivery.order.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeliveryDomainService {

    private final DeliveryRepository deliveryRepository;

    public Delivery create(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }
}
