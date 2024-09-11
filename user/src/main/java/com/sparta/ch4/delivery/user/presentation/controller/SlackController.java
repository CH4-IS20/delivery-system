package com.sparta.ch4.delivery.user.presentation.controller;

import com.sparta.ch4.delivery.user.application.service.SlackService;
import com.sparta.ch4.delivery.user.domain.type.SlackSearchType;
import com.sparta.ch4.delivery.user.presentation.request.SlackSendMessageRequest;
import com.sparta.ch4.delivery.user.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.user.presentation.response.SlackResponse;
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
public class SlackController {

    private final SlackService slackService;

    @PostMapping
    public CommonResponse<SlackResponse> sendSlackMessage(@Valid @RequestBody SlackSendMessageRequest request){
        return CommonResponse.success(slackService.sendSlackMessage(request.toDto()));
    }

    @GetMapping
    public CommonResponse<Page<SlackResponse>> getAllSlackMessages(
            @RequestParam(required = false, name = "searchType") SlackSearchType searchType,
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return CommonResponse.success(slackService.getSlackMessages(searchType, searchValue, pageable));
    }

}
