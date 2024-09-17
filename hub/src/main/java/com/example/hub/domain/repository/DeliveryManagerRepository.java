package com.example.hub.domain.repository;

import com.example.hub.domain.model.DeliveryManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID>,DeliveryManagerRepositoryCustom {

    @Modifying
    @Query("UPDATE DeliveryManager s SET s.deletedAt = :deletedAt, s.isDeleted = true WHERE s.id = :deliveryManagerId")
    void delete(@Param("deliveryManagerId") UUID deliveryManagerId, @Param("deletedAt")LocalDateTime deletedAt);
}
