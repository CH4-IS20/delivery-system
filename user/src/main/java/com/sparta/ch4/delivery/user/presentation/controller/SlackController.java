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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
public class SlackController {

    private final SlackService slackService;

    // TODO: Slack WebHook 연동
//    @PostMapping
//    public CommonResponse<SlackResponse> sendSlackMessage(
//    }


    // FIXME: Slack App에서 보내는 메시지를 받아야 합니다. 현재는 임시로 구현되어 있습니다.
    @PostMapping("/events")
    public CommonResponse<SlackResponse> receiveSlackMessage(
            @Valid @RequestBody SlackSendMessageRequest request,
            @RequestHeader("X-UserId") String userId) {
        return CommonResponse.success(slackService.saveSlackMessage(request.toDto(userId)));
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
