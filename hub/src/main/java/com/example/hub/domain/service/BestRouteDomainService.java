package com.example.hub.domain.service;

import com.example.hub.domain.exception.ApplicationException;
import com.example.hub.domain.exception.ErrorCode;
import com.example.hub.domain.model.DeliveryManager;
import com.example.hub.domain.model.Hub;
import com.example.hub.domain.model.HubRoute;
import com.example.hub.domain.repository.DeliveryManagerRepository;
import com.example.hub.domain.repository.HubRepository;
import com.example.hub.domain.repository.HubRouteRepository;
import com.example.hub.infrastructure.ai.GeminiInterface;
import com.example.hub.infrastructure.ai.GeminiRequest;
import com.example.hub.infrastructure.ai.GeminiResponse;
import com.example.hub.infrastructure.client.CompanyClient;
import com.example.hub.presentation.response.CommonResponse;
import com.example.hub.presentation.response.CompanyResponse;
import com.example.hub.presentation.response.HubRouteForOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BestRouteDomainService {
    public static final String GEMINI_PRO = "gemini-pro";

    private final GeminiInterface geminiInterface;
    private final HubDomainService hubDomainService;
    private final HubRouteDomainService hubRouteDomainService;


    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;

    private final CompanyClient companyClient;
    private final DeliveryManagerRepository deliveryManagerRepository;

    public GeminiResponse getCompletion(GeminiRequest request){
        return geminiInterface.getCompletion(GEMINI_PRO, request);
    }

    // AI를 통해 최적 경로 받아옴
    @Transactional
    public String getAiResponse(String startHubName, String endHubName){
        GeminiRequest geminiRequest = new GeminiRequest(startHubName+"에서 "+endHubName+"를 가야할때, 최적의 경로를 알려줘, 이때 너의 답안은 최적 경로 내용만을 알려주고 고속도로 내용은 빼고 알려주고 이동방향은 쉼표를 통해 구분해서 알려줘, 경로는 내가 준 센터이름들 중에 사용해줘 그리고 시작센터와 도착 센터는 꼭 포함해서 알려줘 \n1. 이동수단 : 트럭\n2. 출발 및 도착시간: 상관없음\n3. 중간 경유지 : 아래의 센터가 존재하므로 이 중에 거쳐야 하는 센터는 아무곳이나 상관없음\n서울특별시센터(서울특별시송파구송파대로55)\n경상남도센터(경남 창원시 의창구 중앙대로 300)\n경기북부센터(경기도 고양시 덕양구 권율대로 570)\n경기남부센터(경기도 이천시 덕평로 257-21)\n부산광역시센터(부산 동구 중앙대로 206)\n대구광역시센터(대구 북구 태평로 161)\n인천광역시센터(인천 남동구 정각로 29)\n광주광역시센터(광주 서구 내방로 111)\n대전광역시센터(대전 서구 둔산로 100)\n울산광역시센터(울산 남구 중앙로 201)\n세종특별자치시센터(세종특별자치시 한누리대로 2130)\n강원특별자치도센터(강원특별자치도 춘천시 중앙로 1)\n충청북도센터(충북 청주시 상당구 상당로 82)\n충청남도센터(충남 홍성군 홍북읍 충남대로 21)\n전북특별자치도센터(전북특별자치도 전주시 완산구 효자로 225)\n전라남도센터(전남 무안군 삼향읍 오룡길 1)\n경상북도센터(경북 안동시 풍천면 도청대로 455)\n4. 우선순위 : 경유 허브 위치");
        GeminiResponse response = getCompletion(geminiRequest);

        String aiResult = response.getCandidates()
                .stream()
                .findFirst().flatMap(candidate -> candidate.getContent().getParts()
                        .stream()
                        .findFirst()
                        .map(GeminiResponse.TextPart::getText))
                .orElse(null);
        return aiResult;
    }

    @Transactional
    public List<HubRouteForOrderResponse> getHubRouteForOrder(UUID supplierId, UUID receiverId, String userId) {
        // 1. 출발 업체/도착 업체에 가까운 허브 찾기
        // 출발 업체, 도착 업체 위/경로 구함
        // TODO :: Company에서 client 요청을 통한 company값 가져오기, UUID로 변경
        // TODO :: Client를 통해 DB 가져오는 과정에서 오류 발생
        CompanyResponse supplierCompany = companyClient.getCompany(supplierId).getData();
        CompanyResponse receiverCompany = companyClient.getCompany(receiverId).getData();

        String startAddress = supplierCompany.address();
        String receiverAddress = receiverCompany.address();

        // ----------------------

        Hub startStore = hubDomainService.getGeocode(startAddress,null);
        Hub endStore = hubDomainService.getGeocode(receiverAddress,null);

        List<Hub> hubList = hubRepository.findAll();

        List<HubRouteForOrderResponse> hubRouteForOrderResponseList = new ArrayList<>();

        // 출발 업체에 대한 가까운 허브 찾기
        double closeDistance = Double.MAX_VALUE;     // 제일 가까운 거리 저장
        double closeDuration = Double.MAX_VALUE;     // 제일 가까운 거리에 해당하는 시간 저장
        String startHubName = "";                    // 제일 가까운 허브 이름 저장
        UUID closeStartHubId = null;

        for(int i=0; i<hubList.size(); i++){
            // 같은 허브일경우 지나침
            if(supplierId.equals(hubList.get(i).getName())){
                continue;
            }
            double[] temp =hubRouteDomainService.naverDirections(startStore.getLongitude() +","+startStore.getLatitude(), hubList.get(i).getLongitude() +","+hubList.get(i).getLatitude());
            double distanceTemp =  temp[0];

            // 기존의 가장 가까운 거리보다 현재 거리가 더 가까우면
            if(closeDistance > distanceTemp){
                closeDistance = distanceTemp;                       // 현재 거리 저장
                closeDuration = temp[1];
                startHubName = hubList.get(i).getName();    // 현재 hub이름 저장
                closeStartHubId = hubList.get(i).getId();
            }

        }

        // 도착 업체에 대한 가까운 허브 찾기
        double furthestDistance = Double.MAX_VALUE;     // 제일 가까운 거리 저장
        double furthestDuration = Double.MAX_VALUE;     // 제일 가까운 거리에 해당하는 시간 저장
        String endHubName = "";                    // 제일 가까운 허브 이름 저장
        UUID endHubId = null;

        for(int i=0; i<hubList.size(); i++){
            // 같은 허브일경우 지나침
            if(receiverId.equals(hubList.get(i).getName())){
                continue;
            }
            double[] temp =  hubRouteDomainService.naverDirections(endStore.getLongitude() +","+endStore.getLatitude(), hubList.get(i).getLongitude() +","+hubList.get(i).getLatitude());
            double distanceTemp = temp[0];


            // 기존의 거리보다 현재 거리가 더 가깝다면
            if(furthestDistance > distanceTemp){
                furthestDistance = distanceTemp;                       // 현재 거리 저장
                furthestDuration = temp[1];
                endHubName = hubList.get(i).getName();    // 현재 hub이름 저장
                endHubId = hubList.get(i).getId();
            }
        }

        // TODO : 네이버 주석 지우면 아래 내용 삭제

        // TODO : 출발지 -> 허브 데이터 저장 Company Client 연동시 해당 정보 저장함
        // 현재 가능한 업체<->허브 담당 배달기사만 찾기 (업체 -> 허브)
        Hub storeToHub = hubRepository.findByNameDelivery(startHubName)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_DELIVERYMANAGER));
        log.info("기사 찾았음...");
        DeliveryManager storeToHubDelivery = storeToHub.getDeliveryManagers().get(0);
        storeToHubDelivery.update(true);        // 배송자 상태 true변경하여 활동중 표시

        // 업체 <-> 허브 데이터 저장
        hubRouteForOrderResponseList.add(HubRouteForOrderResponse.storeStartFrom(supplierCompany.name(),closeStartHubId,storeToHubDelivery.getId(),closeDistance,closeDuration));
//        hubRouteForOrderResponseList.add(HubRouteForOrderResponse.storeStartFrom(null,closeStartHubId,storeToHubDelivery.getId(),closeDistance,closeDuration));

        // 현재 가능한 허브<->업체 담당 배달기사만 찾기(허브 -> 업체)
        Hub hubToStore = hubRepository.findByNameDelivery(endHubName)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_DELIVERYMANAGER));
        log.info("기사 찾았음...");
        DeliveryManager hubToStoreDelivery = hubToStore.getDeliveryManagers().get(0);
        hubToStoreDelivery.update(true);        // 배송자 상태 true변경하여 활동중 표시



        // 2. 최적 경로 찾기
        // -------------------------------------------------------------------------------------------------------------


        String resultText = getAiResponse(startHubName,endHubName);


        // "서울특별시센터 -> 경기북부센터 -> 강원특별자치도센터 \n"

        List<String> routeHub = List.of(resultText.replace(" ","").split(","));


        // HubRoute의 모든 경우의 값들 가져오기
        for(int i=1; i<routeHub.size(); i++){

            String startHubRouteName = routeHub.get(i-1);
            String endHubRouteName = routeHub.get(i);
            log.info("startHubRouteName: {}", startHubRouteName);
            log.info("endHubRouteName: {}", endHubRouteName);
            HubRoute startHubRoute = hubRouteRepository.findFirstByUsernames(startHubRouteName)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.HUBROUTE_NOT_FOUND));
            log.info("startHubRoute :{}", startHubRoute.getStartHubId());
            HubRoute endHubRoute = hubRouteRepository.findFirstByUsernames(endHubRouteName)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.HUBROUTE_NOT_FOUND));
            log.info("endHubRoute :{}", endHubRoute.getEndHubId());
            HubRoute hubRoute;
            UUID startHubId;
            // startHub -> endHub 가는 구조
            if(startHubRoute.getEndHub().size() > endHubRoute.getEndHub().size()){
                hubRoute = startHubRoute.getEndHub().stream()
                        .filter(h -> h.getEndHubName().equals(endHubRouteName))
                        .findFirst()
                        .orElseThrow(() -> new ApplicationException(ErrorCode.HUBROUTE_NOT_FOUND));
                log.info("start -> end");
                startHubId = startHubRoute.getStartHubId();
            }   // endHub -> startHub 가는 구조
            else{
                hubRoute = endHubRoute.getEndHub().stream()
                        .filter(h -> h.getEndHubName().equals(startHubRouteName))
                        .findFirst()
                        .orElseThrow(() -> new ApplicationException(ErrorCode.HUBROUTE_NOT_FOUND));
                log.info("end -> start");
                startHubId = endHubRoute.getStartHubId();
            }

            log.info("로직 통과");
            // 3. 허브 배송 담당 관리자 배정
            // -------------------------------------------------------------------------------------------------------------
            // 현재 배송이 가능하고 허브 담당자만 찾아오기
            List<DeliveryManager> deliveryManager = deliveryManagerRepository.findHubDelievery()
                    .orElseThrow(() -> new ApplicationException(ErrorCode.NO_DELIVERYMANAGER));

            log.info(String.valueOf(deliveryManager.get(0)));

            hubRouteForOrderResponseList.add(HubRouteForOrderResponse.from(hubRoute,deliveryManager.get(0).getId(),startHubId));
        }

        // 허브 <-> 업체 데이터 저장
        hubRouteForOrderResponseList.add(HubRouteForOrderResponse.storeEndFrom(receiverCompany.name(),endHubId,hubToStoreDelivery.getId(),furthestDistance,furthestDuration));
//        hubRouteForOrderResponseList.add(HubRouteForOrderResponse.storeEndFrom(null,endHubId,hubToStoreDelivery.getId(),furthestDistance,furthestDuration));

        return hubRouteForOrderResponseList;
    }

}
