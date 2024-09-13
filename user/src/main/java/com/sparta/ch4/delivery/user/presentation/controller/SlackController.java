package com.sparta.ch4.delivery.user.presentation.controller;

import com.sparta.ch4.delivery.user.application.service.SlackService;
import com.sparta.ch4.delivery.user.domain.type.SlackSearchType;
import com.sparta.ch4.delivery.user.infrastructure.client.SlackMessageClient;
import com.sparta.ch4.delivery.user.presentation.request.SlackSendMessageRequest;
import com.sparta.ch4.delivery.user.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.user.presentation.response.SlackResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
@Tag(name = "Slack Controller", description = "Slack 메시지를 관리하는 API입니다.")
public class SlackController {

    private final SlackService slackService;
    private final SlackMessageClient slackMessageClient;

    @Operation(summary = "슬랙 메시지 전송", description = "지정된 사용자에게 슬랙 메시지를 전송합니다.")
    @ApiResponse(responseCode = "200", description = "메시지가 성공적으로 전송되었습니다.")
    @ApiResponse(responseCode = "400", description = "Slack 메시지 전송 중 문제가 발생했습니다.")
    @PostMapping
    public CommonResponse<SlackResponse> sendSlackMessage(
            @Parameter(description = "슬랙 메시지 요청 데이터") @Valid @RequestBody SlackSendMessageRequest request) {
        return CommonResponse.success(slackMessageClient.sendSlackMessage(request.toDto()));
    }

    @Operation(summary = "모든 슬랙 메시지 조회", description = "옵션으로 검색 조건을 사용하여 슬랙 메시지 목록을 페이지로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "슬랙 메시지가 성공적으로 조회되었습니다.")
    @GetMapping
    public CommonResponse<Page<SlackResponse>> getAllSlackMessages(
            @Parameter(description = "검색할 유형", example = "SLACK_ID") @RequestParam(required = false, name = "searchType") SlackSearchType searchType,
            @Parameter(description = "검색 값") @RequestParam(required = false, name = "searchValue") String searchValue,
            @Parameter(description = "페이지네이션 및 정렬 정보") @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return CommonResponse.success(slackService.getSlackMessages(searchType, searchValue, pageable));
    }
}