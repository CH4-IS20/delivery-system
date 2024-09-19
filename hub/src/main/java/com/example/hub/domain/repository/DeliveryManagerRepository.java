package com.example.hub.domain.repository;

import com.example.hub.domain.model.DeliveryManager;
import com.example.hub.domain.model.Hub;
import com.example.hub.domain.model.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID>,DeliveryManagerRepositoryCustom {

    @Modifying
    @Query("UPDATE DeliveryManager s SET s.deletedAt = :deletedAt, s.isDeleted = true, s.deletedBy = :deletedBy WHERE s.id = :deliveryManagerId")
    void delete(@Param("deliveryManagerId") UUID deliveryManagerId, @Param("deletedAt")LocalDateTime deletedAt, @Param("deletedBy")String deletedBy);


    @Query("SELECT d FROM DeliveryManager d WHERE d.status = false AND d.hub IS NULL")
    Optional<List<DeliveryManager>> findHubDelievery();


}
