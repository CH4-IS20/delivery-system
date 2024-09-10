package com.example.hub.domain.repository;

import com.example.hub.presentation.response.HubResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubRepositoryCustom {
    Page<HubResponse> searchHub(String searchValue, Pageable pageable);
}
