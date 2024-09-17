package com.example.hub.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Entity
@Table(name = "p_hub_route")
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@EntityListeners(value = {AuditingEntityListener.class})
@Where(clause = "is_deleted is false")
public class HubRoute extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID startHubId;
    private String startHubName;
    private UUID endHubId;
    private String endHubName;

    private double distance;            // 현재 허브로부터 거리

    private double durationTime;     // 현재 허브로부터 이동 시간

    @JsonBackReference // 부모 카테고리의 순환 참조 방지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private HubRoute parent;         // 부모 id

    // 출발지부터 모든 허브에 대한 경우의 수 저장
    @JsonManagedReference // 자식 카테고리의 순환 참조 방지
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HubRoute> endHub;


    // 출발 허브 생성(모든 경우의 수의 시작점이 될 허브)
    public static HubRoute fromHub(Hub hub) {
        return HubRoute.builder()
                .startHubId(hub.getId())
                .startHubName(hub.getName())
                .parent(null)
                .distance(0)
                .durationTime(0)
                .build();
    }

    // 자식 허브 추가
    public void addEndHubId(HubRoute endHubRoute) {
        if(endHub == null){
            endHub = new ArrayList<>();
        }
        endHub.add(endHubRoute);
    }
}



/*
public record HubRouteForOrderResponse(
        UUID hubRouteId,  // 허브 경로 엔티티 ID
        UUID deliveryManagerId,  //배송담당자
        UUID startHubId, // 시작 허브
        UUID destHubId, //  목적지 허브
        Float estimatedDistance,   // 예상 거리
        Integer estimatedDuration   //예상 소요 시간
) {
}
 */