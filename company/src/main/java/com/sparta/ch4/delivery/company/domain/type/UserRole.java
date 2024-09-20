package com.sparta.ch4.delivery.company.domain.type;

/**
 * 시스템 내 사용자 역할을 나타내는 Enum입니다.
 *
 * <ul>
 *   <li><b>MASTER</b>: 시스템 내 모든 권한을 가집니다.</li>
 *   <li><b>HUB_MANAGER</b>: 자신이 소속된 허브에 속한 업체들만 관리할 수 있습니다.</li>
 *   <li><b>HUB_DELIVERY</b>: 허브 간 배송을 담당하는 역할입니다.</li>
 *   <li><b>HUB_COMPANY</b>: 자신의 업체 정보만 수정 가능하며, 다른 업체에 대해서는 조회 및 검색만 가능합니다.</li>
 * </ul>
 */
public enum UserRole {
    MASTER,       // 마스터 관리자
    HUB_MANAGER,  // 허브 관리자
    HUB_DELIVERY, // 허브 배송담당자
    HUB_COMPANY   // 허브 업체 관리자
}