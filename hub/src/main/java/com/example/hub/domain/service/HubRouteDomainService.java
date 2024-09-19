package com.example.hub.domain.service;

import com.example.hub.application.dto.EndHubRouteDto;
import com.example.hub.domain.exception.ApplicationException;
import com.example.hub.domain.exception.ErrorCode;
import com.example.hub.domain.model.Hub;
import com.example.hub.domain.model.HubRoute;
import com.example.hub.domain.naver.NaverResponse;
import com.example.hub.domain.repository.HubRepository;
import com.example.hub.domain.repository.HubRouteRepository;
import com.example.hub.presentation.response.HubRouteResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubRouteDomainService {

    @Value("${naver.access.key}")
    private String NAVER_API_KEY;// 실제 서버에서 구동할때는 무조건 환경변수에 숨겨야함 절대 노출되면 안됨!!!
    @Value("${naver.secret.key}")
    private String NAVER_SECRET_KEY;// 실제 서버에서 구동할때는 무조건 환경변수에 숨겨야함 절대 노출되면 안됨!!!

    private final HubDomainService hubDomainService;

    private final HubRouteRepository hubRouteRepository;
    private final HubRepository hubRepository;

    private final RestTemplate restTemplate;


    // 네이버 시간, 거리 계산
    public double[] naverDirections(String start, String goal){

        String url = String.format("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?start=%s&goal=%s&option=trafast", start, goal);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", NAVER_API_KEY);
        headers.set("X-NCP-APIGW-API-KEY", NAVER_SECRET_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // TODO :: naver url 통신 오류
        NaverResponse naverResponse = restTemplate.exchange(url, HttpMethod.GET, entity, NaverResponse.class).getBody();

        log.info(naverResponse.toString());

        double temp[] = new double[2];

        if(naverResponse.getRoute() == null){
            temp[0] = 0;
            temp[1] = 0;
        }else{
            double distance = naverResponse.getRoute().getTrafast()[0].getSummary().getDistance()/1000;     // m -> km
            double duration = naverResponse.getRoute().getTrafast()[0].getSummary().getDuration()/1000;     // m/s -> s

            temp[0] = distance;
            temp[1] = duration;
        }

        return temp;
    }


    // 허브 경로 생성
    public List<HubRouteResponse> createHubRoute(String userId) {


        List<Hub> hubList = hubDomainService.getHubAll();

        /* Hub에 존재하는 허브 수만큼 반복함
            1. 각 허브별 다른 허브로 이동할때의 거리와 시간을 구해야함
            2. hublist에서 맨 처음값을 가져와 hubRoute의 시작 허브로 기준을 잡고
            3. 나머지 hub들을 도착지 허브로 하여 자식 엔티티로 구성하여 저장시킴
            4. 이를 list size 만큼 반복함(이때, i를 1씩 증가시켜 중복되는 값을 제거함)
         */

        for(int i=0; i<hubList.size(); i++){
            Hub hub = hubList.get(i);
            HubRoute hubRoute = HubRoute.fromHub(hub,userId);      // 기준 시작 hub 생성

            for(int j=i+1; j<hubList.size(); j++){
                double[] temp = new double[2];

                // naver map API 사용하여 거리, 시간 구함
                temp = naverDirections(hub.getLongitude()+","+hub.getLatitude(),hubList.get(j).getLongitude()+","+hubList.get(j).getLatitude());

                EndHubRouteDto dto = EndHubRouteDto.from(hubList.get(j),temp[0],temp[1]);
                HubRoute childEndHubRoute = EndHubRouteDto.to(dto,hubRoute);             // Dto -> HubRoute(자식 Entity)
                hubRoute.addEndHubId(childEndHubRoute);                 // 기준 시작 hub에 자식 HubRoute 추가

            }
            hubRouteRepository.save(hubRoute);
        }
        List<HubRoute> hubRouteFindList = findHubRouteAll();
        List<HubRouteResponse> hubRouteList =  hubRouteFindList.stream()
                .filter(h -> h.getParent() == null)
                .map(h -> HubRouteResponse.from(h))
                .collect(Collectors.toList());

        return hubRouteList;
    }

    // 허브 경로 전체 출력
    @Cacheable("hubRouteStore")
    public List<HubRoute> findHubRouteAll(){
        return hubRouteRepository.findAll();
    }

    // 특정 허브에 대한 모든 값 찾기
    public List<HubRouteResponse> searchHubRouteList(UUID startHubId) {

        if(startHubId == null){
            List<HubRoute> hubRouteFindList = findHubRouteAll();
            List<HubRouteResponse> hubRouteList =  hubRouteFindList.stream()
                    .filter(h -> h.getParent() == null)
                    .map(h -> HubRouteResponse.from(h))
                    .collect(Collectors.toList());

            return hubRouteList;
        }else{
            HubRoute response = hubRouteRepository.findByStartHubId(startHubId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.NOTFOUND_VALUE));

            return Collections.singletonList(HubRouteResponse.from(response));
        }
    }

    // 허브 삭제
    @CacheEvict("hubRouteStore")
    public void deleteHubRoute(UUID startHubId,String userId) {
        LocalDateTime now = LocalDateTime.now();

        // TODO : User 권한 검증 코드 추가 (마스터 관리자만 가능)

        // TODO : 예외처리 Custom으로 변경 예정
        HubRoute response = hubRouteRepository.findByStartHubId(startHubId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOTFOUND_VALUE));

        // DeleteAt, DeleteBy값 넣어주기 위해 delete메서드 custom
        hubRouteRepository.delete(response.getId(),now,userId);
    }
}
