package com.example.hub.application.service;

import com.example.hub.domain.service.HubRouteDomainService;
import com.example.hub.presentation.response.HubRouteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRoutService {

    private final HubRouteDomainService hubRouteDomainService;

    @Transactional
    public List<HubRouteResponse> createHubRoute(String userId) {
        return hubRouteDomainService.createHubRoute(userId);
    }

    @Transactional(readOnly = true)
    public List<HubRouteResponse> searchHubRouteList(UUID startHubId) {
        return hubRouteDomainService.searchHubRouteList(startHubId);
    }

    @Transactional
    public void deleteHubRoute(UUID startHubId,String userId) {
        hubRouteDomainService.deleteHubRoute(startHubId,userId);
    }
}
