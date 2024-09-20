package com.sparta.ch4.delivery.order.domain.type;

public enum DeliveryStatus {
    HUB_WAITING,  // 허브에서 출발을 기다리는 상태
    HUB_MOVING, //  허브에서 다음 허브로 이동 중인 상태
    DEST_HUB_ARRIVED,  // 목적지 허브에 도착한 상태
    IN_DELIVERY  // 배송 중인 상태
}


