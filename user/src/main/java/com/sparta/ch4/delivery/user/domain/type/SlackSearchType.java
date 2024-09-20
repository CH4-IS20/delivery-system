package com.sparta.ch4.delivery.user.domain.type;

public enum SlackSearchType {
    SLACK_ID,         // Slack ID로 검색 (특정 사용자 또는 수신자의 메시지)
    MESSAGE_CONTENT,  // 메시지 내용으로 검색 (특정 키워드를 포함한 메시지)
    // TODO: 특정 날짜에 메세지 검색?
}

