package com.sparta.ch4.delivery.order.domain.model;


import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Entity
@Table(name = "p_delivery_history")
public class DeliveryHistory extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Column(name = "delivery_manager_id", nullable = false)
    private UUID deliveryManagerId;     // 배송담당자 ID

    @Column(name = "hub_route_id", nullable = false)
    private UUID hubRouteId;    // 허브 경로 ID

    @Column(name = "sequence", nullable = false)
    private Integer sequence;   // 배송 경로 상 허브의 순번

    @Column(name = "estimated_distance", nullable = false)
    private Float estimatedDistance;    // 예상 거리

    @Column(name = "estimated_duration", nullable = false)
    private Integer estimatedDuration;   //예상 소요 시간

    @Setter
    @Column(name = "actual_distance")
    private Float actualDistance;

    @Setter
    @Column(name = "actual_duration")
    private Integer actualDuration;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;
}
