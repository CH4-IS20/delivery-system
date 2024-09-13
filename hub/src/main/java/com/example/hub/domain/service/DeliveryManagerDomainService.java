package com.example.hub.domain.service;

import com.example.hub.domain.exception.ApplicationException;
import com.example.hub.domain.exception.ErrorCode;
import com.example.hub.domain.model.DeliveryManager;
import com.example.hub.domain.model.Hub;
import com.example.hub.domain.repository.DeliveryManagerRepository;
import com.example.hub.domain.repository.HubRepository;
import com.example.hub.domain.type.DeliveryManagerType;
import com.example.hub.presentation.request.DeliveryManagerCreateRequest;
import com.example.hub.presentation.response.DeliveryManagerResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryManagerDomainService {
    private final DeliveryManagerRepository deliveryManagerRepository;
    private final HubRepository hubRepository;

    // 배송 담당자 생성
    public DeliveryManagerResponse createDeliveryManager(DeliveryManagerCreateRequest request) {
        // 소속이 업체 배송 담당자라면 해당 허브가 존재하는지 확인
        DeliveryManager deliveryManager;

        // 이미 다른 곳에 할당된 배송 담당자인지 확인
        if(deliveryManagerRepository.findById(request.userId()).isPresent()){
            throw new ApplicationException(ErrorCode.DELIVERYMANAGER_DUPLICATED);
        }

        if(request.type() == DeliveryManagerType.StoreDelivery){
            Hub hub = hubRepository.findById(request.hubId())
                        .orElseThrow(() -> new ApplicationException(ErrorCode.HUB_NOT_FOUND));

            // 해당 허브에 인원이 10명 미만인지 체크
            if(hub.getDeliveryManagers().size() < 10){
                deliveryManager = DeliveryManagerCreateRequest.to(request,hub);

                deliveryManagerRepository.save(deliveryManager);
            }else{
                throw new ApplicationException(ErrorCode.DELIVERY_OVERCROWDED);
            }
        }else{
            deliveryManager = DeliveryManagerCreateRequest.to(request,null);        // 허브 이동 담당자는 제한된 허브가 없으므로 hub쪽은 null값 넣어줌
            deliveryManagerRepository.save(deliveryManager);
        }

        return DeliveryManagerResponse.from(deliveryManager);
    }

    // 배송담당자 세부 조회
    public DeliveryManagerResponse readDeliveryManager(UUID deliveryManagerId,String userId,String role) {

        // 현재 찾고 싶은 deliveryMangerId값을 통해 찾아온 데이터
        DeliveryManager deliveryManager = deliveryManagerRepository.findById(deliveryManagerId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.DELIVERYMANAGER_NOT_FOUND));
        
        // TODO : 권한이 허브 관리자일 경우 해당 허브 구분 / user부분에 상세 hub값 필요

        // 배송담당자일 경우
        if(role.equals("HUB_DELIVERY")){
            // 배송담당자이지만 현재 로그인한 사람의 정보가 아닐 경우
            if(deliveryManagerId != UUID.fromString(userId)){
                throw new ApplicationException(ErrorCode.ACCESS_DENIED);
            }
        }

        return DeliveryManagerResponse.from(deliveryManager);
    }

    // 특정 허브에 대한 전체 조회
    public Page<DeliveryManagerResponse> searchDeliveryManager(String searchValue, Pageable pageable) {
        return deliveryManagerRepository.searchHub(searchValue,pageable);
    }

    // 배송 담당자 수정
    public DeliveryManagerResponse updateDeliveryManager(DeliveryManagerCreateRequest request, UUID deliveryManagerId, String userId, String role) {

        // TODO : 권한이 허브 관리자일경우 해당 허브 구분(본인의 허브안의 배송 담당자만 관리 가능)
        DeliveryManager deliveryManager = deliveryManagerRepository.findById(deliveryManagerId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.DELIVERYMANAGER_NOT_FOUND));
        
        Hub hub;

        // 허브가 변경되었는지 확인, 만약 변경되었으면 새로운 허브 객체 생성
        // 1. 허브 변경 확인 체크
        // 2. 변경된 허브에 배정된 인원 체크
        // TODO : 허브 id값 변경 안됨
        if(deliveryManager.getHub().equals( request.hubId())){
            hub = hubRepository.findById(request.hubId())
                    .orElseThrow(() -> new ApplicationException(ErrorCode.HUB_NOT_FOUND));

            // 해당 허브에 인원이 10명미만 인지 체크
            if(hub.getDeliveryManagers().size() >= 10){
                throw new ApplicationException(ErrorCode.DELIVERY_OVERCROWDED);
            }

            deliveryManager.update(request,hub);
        }else{
            deliveryManager.update(request,null);
        }

        return DeliveryManagerResponse.from(deliveryManager);
    }

    // 배송 담당자 삭제
    public void deleteDeliveryManager(UUID deliveryManagerId, String userId, String role) {

        LocalDateTime now = LocalDateTime.now();


        // TODO : 권한이 허브 관리자일경우 해당 허브 구분(본인의 허브안의 배송 담당자만 관리 가능)
        DeliveryManager deliveryManager = deliveryManagerRepository.findById(deliveryManagerId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.DELIVERYMANAGER_NOT_FOUND));

        deliveryManagerRepository.delete(deliveryManagerId,now);
    }
}
