package com.example.hub.domain.model;

import com.example.hub.domain.type.DeliveryManagerType;
import com.example.hub.presentation.request.DeliveryManagerCreateRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
@Entity
@Table(name = "p_delivery_manager")
@Where(clause = "is_deleted is false")
public class DeliveryManager extends BaseEntity {

    @Id
    private UUID id;            // 사용자 id와 동일

    private String name;        // 배송 담당자 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id")
    private Hub hub;

    private String slackId;     // 배송 담당자 슬랙 ID

    private DeliveryManagerType type;

    private boolean status;     // 배송 가능한 상태인지 아닌지 구분   (기본값 false)


    public void update(DeliveryManagerCreateRequest request, Hub hub,String userId) {
        this.name = request.name() != null ? request.name() : this.name;
        this.hub = hub != null ? hub : this.hub;
        this.slackId = request.slackId() != null ? request.slackId() : this.slackId;
        this.type = request.type() != null ? request.type() : this.type;
        setUpdatedBy(userId);
    }

    // 배송 담당자 현재 상태 변경
    public void update(boolean status){
        this.status = status;
    }

}
