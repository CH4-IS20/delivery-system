package com.example.hub.domain.service;

import com.example.hub.application.dto.HubDto;
import com.example.hub.application.geocode.GoogleMapResponse;
import com.example.hub.domain.exception.ApplicationException;
import com.example.hub.domain.exception.ErrorCode;
import com.example.hub.domain.model.Hub;
import com.example.hub.domain.repository.HubRepository;
import com.example.hub.domain.type.HubAddress;
import com.example.hub.presentation.request.HubCreateRequest;
import com.example.hub.presentation.response.HubResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubDomainService {

    @Value("${google.map.key}")
    private Object API_KEY;// 실제 서버에서 구동할때는 무조건 환경변수에 숨겨야함 절대 노출되면 안됨!!!

    private final HubRepository hubRepository;


    // 위/경도 구하는 중복 코드 분리
    public Hub getGeocode(String hubName,String userId) {
        // Enum에서 Hub에 따른 해당 주소 가져옴
        String hubAddress = "";

        if(hubName.contains("센터")){
            for(HubAddress hubNames: HubAddress.values()){
                if(hubNames.name().equals(hubName)){
                    hubAddress = hubNames.getCenterAddress();
                }
            }
        }else{
            hubAddress = hubName;
        }


        UriComponents uri = UriComponentsBuilder.newInstance()          // UriComponentsBuilder.newInstance = uri 주소를 직접 조립하여 만든다
                // https://maps.googleapis.com/maps/api/geocode/json?address="address"&key="API_KEY"와 같음
                // 위 처럼 한번에 사용하지 않고 조립해서 사용하는 이유는 address나 key값처럼 외부에서 값을 받아올때 쉽게 넣어 조립이 가능하기 때문
                .scheme("https")
                .host("maps.googleapis.com")
                .path("/maps/api/geocode/json")
                .queryParam("key",API_KEY)
                .queryParam("address",hubAddress)
                .build();

        GoogleMapResponse response = new RestTemplate().getForEntity(uri.toUriString(), GoogleMapResponse.class).getBody();     // 구글 map api에서 반환해주는 json형식을 MapResponse클래스 형식에 맞춰 받아옴
        Double lat = Arrays.stream(response.getResult()).findFirst().get().getGeometry().getLocation().getLat();
        Double lng =Arrays.stream(response.getResult()).findFirst().get().getGeometry().getLocation().getLng();

        return HubCreateRequest.toEntity(hubName,hubAddress,lat,lng,userId);
    }


    // 허브 생성
    public HubResponse createHub(HubCreateRequest request,String userId) {
        // TODO : User 권한 검증 코드 추가 (마스터 관리자만 가능)

        // 이미 해당 이름의 허브가 존재한다면
        if(hubRepository.findByName(request.name()).isPresent()){
            throw new ApplicationException(ErrorCode.DUPLICATED_HUBNAME);
        }


        Hub hub = hubRepository.save(getGeocode(request.name(),userId));

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

    // 허브 전체 조회

    @Cacheable("hubStore")
    public List<Hub> getHubAll(){
        return hubRepository.findAll();
    }

    // 허브 상세 전체 조회
    public Page<HubResponse> searchHubList(String searchValue, Pageable pageable) {

        return hubRepository.searchHub(searchValue,pageable);
    }

    // 허브 수정
    @CacheEvict("hubStore")
    public HubResponse updateHub(UUID id, HubCreateRequest request,String userId) {
        // TODO : User 권한 검증 코드 추가 (마스터 관리자만 가능)

        // TODO : 예외처리 Custom으로 변경 예정
        Hub hub = hubRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hub not found"));

        Hub updateHub = null;

        // 만약 수정할려고 입력받은 주소 값이 존재하면서 해당 값이 hub에 저장된 값과 다를 경우
        // 새로운 주소이므로 위/경도 다시 받아와야함
        if(request.name() != null){
            updateHub = getGeocode(request.name(),userId);
            hub.update(request,updateHub.getAddress(),updateHub.getLatitude(),updateHub.getLongitude(),userId);
        }else{
            throw new ApplicationException(ErrorCode.NOTFOUND_VALUE);
        }

        return HubResponse.fromDto(HubDto.fromEntity(hub));
    }

    // 허브 삭제
    @CacheEvict("hubStore")
    public void deleteHub(UUID id,String userId) {
        LocalDateTime now = LocalDateTime.now();

        // TODO : User 권한 검증 코드 추가 (마스터 관리자만 가능)

        // TODO : 예외처리 Custom으로 변경 예정
        Hub hub = hubRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hub not found"));

        // DeleteAt, DeleteBy값 넣어주기 위해 delete메서드 custom
        hubRepository.delete(hub.getId(),now,userId);
    }
}
