package com.example.hub.application.service;

import com.example.hub.domain.service.BestRouteDomainService;
import com.example.hub.domain.service.DeliveryManagerDomainService;
import com.example.hub.domain.service.HubDomainService;
import com.example.hub.presentation.request.HubCreateRequest;
import com.example.hub.presentation.response.HubResponse;
import com.example.hub.presentation.response.HubRouteForOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubDomainService hubDomainService;
    private final BestRouteDomainService bestRouteDomainService;

    // 허브 생성
    @Transactional
    public HubResponse createHub(HubCreateRequest request){
        return hubDomainService.createHub(request);
    }

    // 허브 상세 조회
    @Transactional(readOnly = true)
    public HubResponse getHub(UUID id) {
        return hubDomainService.getHub(id);
    }

    // 허브 검색 리스트
    @Transactional(readOnly = true)
    public Page<HubResponse> searchHubList(String searchValue, Pageable pageable) {
        return hubDomainService.searchHubList(searchValue,pageable);
    }

    // 허브 수정
    @Transactional
    public HubResponse updateHub(UUID id, HubCreateRequest request) {
        return hubDomainService.updateHub(id,request);
    }

    // 허브 삭제
    @Transactional
    public void deleteHub(UUID id) {
        hubDomainService.deleteHub(id);
    }

    // Order에 따른
    public List<HubRouteForOrderResponse> getHubRouteForOrder(String supplierId, String receiverId, String userId) {
        return bestRouteDomainService.getHubRouteForOrder(supplierId,receiverId,userId);
    }
}
