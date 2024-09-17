package com.example.hub.domain.model;


import com.example.hub.domain.type.HubAddress;
import com.example.hub.presentation.request.HubCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@EntityListeners(value = {AuditingEntityListener.class})
@Table(name = "p_hub")
@Builder
@Where(clause = "is_deleted is false")
public class Hub extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name= "name", nullable = false, unique = true)
    private String name;

    @Column(name= "address", nullable = false, unique = true)
    private String address;

    private double latitude;
    private double longitude;

    @OneToMany(mappedBy = "hub" , cascade = CascadeType.ALL,orphanRemoval = true)
    private List<DeliveryManager> deliveryManagers;

    public void update(HubCreateRequest request, String address, double lat, double lon) {
        this.name = request.name();
        this.address =address ;
        this.latitude = lat;
        this.longitude = lon;
    }
}
