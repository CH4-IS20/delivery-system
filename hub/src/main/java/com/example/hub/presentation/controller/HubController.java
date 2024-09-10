package com.example.hub.presentation.controller;

import com.example.hub.application.service.HubService;
import com.example.hub.presentation.request.HubCreateRequest;
import com.example.hub.presentation.request.HubUpdateRequest;
import com.example.hub.presentation.response.HubResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
@Tag(name = "HUB API", description = "허브 CRUD")
public class HubController {

    private final HubService hubService;

    // 허브 생성
    @PostMapping
//    @PreAuthorize("hasRole('ROLE_OWNER')")              // 권한 체크
    @Operation(summary = "허브 생성", description = "유저에 대한 허브 생성(허브 세부내용 작성)")
    public HubResponse createHub(@RequestBody HubCreateRequest request){
        // TODO :: Response로 감싸주고, UserId를 Header에서 X-UserId를 통해 받아오는 코드 수정 예정
        return hubService.createHub(request);
    }
    
    // 허브 조회
    @GetMapping("/{hubId}")
    @Operation(summary = "허브 세부 조회", description = "특정 허브에 대한 세부 조회")
    public HubResponse getHub(@PathVariable(name = "hubId") UUID id){
        // TODO :: Response로 감싸주는 코드 추가
        return hubService.getHub(id);
    }
    
    // 허브 검색 전체 조회
    // 검색은 허브이름을 통해 가능
    @GetMapping
    @Operation(summary = "허브 이름 검색 조회", description = "허브 이름에 대한 검색 조회")
    public Page<HubResponse> searchHubs(
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @PageableDefault(size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        // TODO :: Response로 감싸주는 코드 추가

        return hubService.searchHubList(searchValue,pageable);
    }

    // 허브 수정
    @PatchMapping("/{hubId}")
    @Operation(summary = "허브 수정", description = "특정 허브에 대한 수정")
    public HubResponse updateHub(@PathVariable(name = "hubId") UUID id, @RequestBody HubUpdateRequest request){
        // TODO :: Response로 감싸주고, UserId를 Header에서 X-UserId를 통해 받아오는 코드 수정 예정

        return hubService.updateHub(id,request);
    }


    // 허브 삭제
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{hubId}")
    @Operation(summary = "허브 삭제", description = "특정 허브에 대한 삭제")
    public void deleteHub(@PathVariable(name = "hubId") UUID id){
        // TODO :: Response로 감싸주고, UserId를 Header에서 X-UserId를 통해 받아오는 코드 수정 예정

        hubService.deleteHub(id);
    }


}
