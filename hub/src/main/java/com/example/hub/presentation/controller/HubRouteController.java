package com.example.hub.presentation.controller;

import com.example.hub.application.service.HubRoutService;
import com.example.hub.domain.model.Hub;
import com.example.hub.presentation.response.CommonResponse;
import com.example.hub.presentation.response.HubRouteResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hub-routes")
@RequiredArgsConstructor
public class HubRouteController {

    private final HubRoutService hubRoutService;

    // 허브경로 생성
    @GetMapping()
    public CommonResponse<List<HubRouteResponse>> createHubRoute(
            @RequestHeader(value = "X-UserId", required = true) String userId
    ){

        return CommonResponse.success(hubRoutService.createHubRoute(userId));
    }


    // 허브경로 전체 출력  (출발 허브 ID를 통한 허브별 데이터 호출 가능)
    @GetMapping("/list")
    public CommonResponse<List<HubRouteResponse>> getHubRoute(
            @RequestParam(required = false, name = "startHubId") UUID startHubId
    ){
        return CommonResponse.success(hubRoutService.searchHubRouteList(startHubId));
    }


    // 허브경로 삭제 (출발지 허브id를 통해 해당 관련된 데이터 모두 삭제)
    @DeleteMapping()
    public CommonResponse<String> deleteHubRoute(
            @RequestParam(required = false, name = "startHubId") UUID startHubId,
            @RequestHeader(value = "X-UserId", required = true) String userId
    ){
        hubRoutService.deleteHubRoute(startHubId,userId);
        return CommonResponse.success(startHubId+"에 해당하는 데이터가 삭제되었습니다");
    }
}
