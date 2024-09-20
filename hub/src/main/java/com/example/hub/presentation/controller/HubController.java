package com.example.hub.presentation.controller;

import com.example.hub.application.service.HubService;
import com.example.hub.presentation.request.HubCreateRequest;
import com.example.hub.presentation.response.CommonResponse;
import com.example.hub.presentation.response.HubResponse;
import com.example.hub.presentation.response.HubRouteForOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    public CommonResponse<HubResponse> createHub(
            @RequestBody HubCreateRequest request,
            @RequestHeader(value = "X-UserId", required = true) String userId
    ){
        return CommonResponse.success(hubService.createHub(request,userId));
    }

    // 허브 조회
    @GetMapping("/{hubId}")
    @Operation(summary = "허브 세부 조회", description = "특정 허브에 대한 세부 조회")
    public CommonResponse<HubResponse> getHub(@PathVariable(name = "hubId") UUID id){

        return CommonResponse.success(hubService.getHub(id));
    }

    // 허브 검색 전체 조회
    // 검색은 허브이름을 통해 가능
    @GetMapping
    @Operation(summary = "허브 이름 검색 조회", description = "허브 이름에 대한 검색 조회")
    public CommonResponse<Page<HubResponse>> searchHubs(
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @PageableDefault(size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable){

        return CommonResponse.success(hubService.searchHubList(searchValue,pageable));
    }

    // 허브 수정
    @PatchMapping("/{hubId}")
    @Operation(summary = "허브 수정", description = "특정 허브에 대한 수정")
    public CommonResponse<HubResponse> updateHub(
            @PathVariable(name = "hubId") UUID id,
            @RequestBody HubCreateRequest request,
            @RequestHeader(value = "X-UserId", required = true) String userId
    ){

        return CommonResponse.success(hubService.updateHub(id,request,userId));
    }


    // 허브 삭제
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{hubId}")
    @Operation(summary = "허브 삭제", description = "특정 허브에 대한 삭제")
    public CommonResponse<String> deleteHub(
            @PathVariable(name = "hubId") UUID id,
            @RequestHeader(value = "X-UserId", required = true) String userId
    ){

        hubService.deleteHub(id,userId);

        return CommonResponse.success(id+"허브에 해당하는 데이터가 삭제되었습니다");
    }

    // Order에서 주문받아옴
    @GetMapping("/order")
    public CommonResponse<List<HubRouteForOrderResponse>> getHubRouteForOrder(
            @RequestParam(name = "supplierId") UUID supplierId,
            @RequestParam(name = "receiverId") UUID receiverId,
            @RequestHeader(value = "X-UserId", required = true) String userId
    ){
        return CommonResponse.success(hubService.getHubRouteForOrder(supplierId,receiverId,userId));
    }
}
