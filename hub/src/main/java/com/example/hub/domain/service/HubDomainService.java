package com.example.hub.domain.service;

import com.example.hub.application.dto.HubDto;
import com.example.hub.application.geocode.GoogleMapResponse;
import com.example.hub.domain.model.Hub;
import com.example.hub.domain.repository.HubRepository;
import com.example.hub.domain.type.HubAddress;
import com.example.hub.presentation.request.HubCreateRequest;
import com.example.hub.presentation.request.HubUpdateRequest;
import com.example.hub.presentation.response.HubResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubDomainService {

    @Value("${google.map.key}")
    private Object API_KEY;// 실제 서버에서 구동할때는 무조건 환경변수에 숨겨야함 절대 노출되면 안됨!!!

    private final HubRepository hubRepository;


    // 위/경도 구하는 중복 코드 분리
    public Hub getGeocode(String hubName,String address) {
        // Enum에서 Hub에 따른 해당 주소 가져옴
        String hubAddress = "";
        for(HubAddress hubNames: HubAddress.values()){
            if(hubNames.name().equals(hubName)){
                hubAddress = hubNames.getCenterAddress();
            }
        }

        String resultAddress = address == null ? hubAddress : address;

        UriComponents uri = UriComponentsBuilder.newInstance()          // UriComponentsBuilder.newInstance = uri 주소를 직접 조립하여 만든다
                // https://maps.googleapis.com/maps/api/geocode/json?address="address"&key="API_KEY"와 같음
                // 위 처럼 한번에 사용하지 않고 조립해서 사용하는 이유는 address나 key값처럼 외부에서 값을 받아올때 쉽게 넣어 조립이 가능하기 때문
                .scheme("https")
                .host("maps.googleapis.com")
                .path("/maps/api/geocode/json")
                .queryParam("key",API_KEY)
                .queryParam("address",resultAddress)
                .build();

        GoogleMapResponse response = new RestTemplate().getForEntity(uri.toUriString(), GoogleMapResponse.class).getBody();     // 구글 map api에서 반환해주는 json형식을 MapResponse클래스 형식에 맞춰 받아옴
        Double lat = Arrays.stream(response.getResult()).findFirst().get().getGeometry().getLocation().getLat();
        Double lng =Arrays.stream(response.getResult()).findFirst().get().getGeometry().getLocation().getLng();

        return HubCreateRequest.toEntity(hubName,resultAddress,lat,lng);
    }


    // 허브 생성
    public HubResponse createHub(HubCreateRequest request) {
        // TODO : User 권한 검증 코드 추가 (마스터 관리자만 가능)

        Hub hub = hubRepository.save(getGeocode(request.name(),null));

        HubDto dto = HubDto.fromEntity(hub);

        return HubResponse.fromDto(dto);
    }


    // 허브 상세 조회
    public HubResponse getHub(UUID id) {
        // TODO : 예외처리 Custom으로 변경 예정
        Hub hub = hubRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hub not found"));

        HubDto dto = HubDto.fromEntity(hub);

        return HubResponse.fromDto(dto);
    }
    
    // 허브 상세 전체 조회
    public Page<HubResponse> searchHubList(String searchValue, Pageable pageable) {

        return hubRepository.searchHub(searchValue,pageable);
    }
    
    // 허브 수정
    public HubResponse updateHub(UUID id, HubUpdateRequest request) {
        // TODO : User 권한 검증 코드 추가 (마스터 관리자만 가능)

        // TODO : 예외처리 Custom으로 변경 예정
        Hub hub = hubRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hub not found"));

        Hub updateHub = null;

        // 만약 수정할려고 입력받은 주소 값이 존재하면서 해당 값이 hub에 저장된 값과 다를 경우
        // 새로운 주소이므로 위/경도 다시 받아와야함
        if((request != null)&&(!request.address().equals(hub.getAddress()))){
            // 허브의 이름도 수정이 되었을 경우
            if(request.name() != null){
                updateHub = getGeocode(request.name(),request.address());
            }else{      // 허브의 이름은 수정되지 않았을 경우
                updateHub = getGeocode(hub.getName(),request.address());
            }
            hub.update(request,updateHub.getLatitude(), updateHub.getLongitude());
        }else if(request.name() != null){
            hub.update(request,hub.getLatitude(), hub.getLongitude());
        }

        return HubResponse.fromDto(HubDto.fromEntity(hub));
    }

    // 허브 삭제
    public void deleteHub(UUID id) {
        LocalDateTime now = LocalDateTime.now();

        // TODO : User 권한 검증 코드 추가 (마스터 관리자만 가능)

        // TODO : 예외처리 Custom으로 변경 예정
        Hub hub = hubRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hub not found"));

        // DeleteAt, DeleteBy값 넣어주기 위해 delete메서드 custom
        hubRepository.delete(hub.getId(),now);
    }
}
