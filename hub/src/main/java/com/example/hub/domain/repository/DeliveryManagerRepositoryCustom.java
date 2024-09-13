package com.example.hub.domain.repository;

import com.example.hub.presentation.response.DeliveryManagerResponse;
import com.example.hub.presentation.response.HubResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryManagerRepositoryCustom {
    Page<DeliveryManagerResponse> searchHub(String searchValue, Pageable pageable);
}
