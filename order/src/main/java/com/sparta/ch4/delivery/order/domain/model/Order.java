package com.sparta.ch4.delivery.order.domain.model;


import com.sparta.ch4.delivery.order.domain.type.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Entity
@Table(name = "p_order")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Setter
    @Column(name = "user_id", nullable = false)
    private Long userId;  //주문자 ID

    @Setter
    @Column(name = "supplier_id", nullable = false)
    private UUID supplierId; //요청 업체 ID (공급 업체)

    @Setter
    @Column(name = "receiver_id")
    private UUID receiverId;	 // 수령 업체 ID

    @Setter
    @Column(name = "product_id")
    private UUID productId;

    @Setter
    private Integer quantity;	 //	주문 수량

    @Setter
    private LocalDateTime orderDate;

    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;	 // 	주문 상태 (pending, completed, canceled)

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Delivery delivery;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
