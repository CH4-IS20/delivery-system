package com.sparta.ch4.delivery.order.domain.model;


import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Entity
@Table(name = "p_delivery")
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @Column(name = "start_hub", nullable = false)
    private UUID startHub;

    @Column(name = "end_hub", nullable = false)
    private UUID endHub;

    @Column(name = "delivery_address", length = 100, nullable = false)
    private String deliveryAddress;

    @Column(name = "recipient", length = 100, nullable = false)
    private String recipient;  // 수령인

    @Column(name = "recipient_slack", nullable = false)
    private UUID recipientSlack;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(id, delivery.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
