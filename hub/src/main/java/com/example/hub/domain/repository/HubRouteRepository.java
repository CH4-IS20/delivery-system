package com.example.hub.domain.repository;

import com.example.hub.domain.model.HubRoute;
import com.example.hub.presentation.response.HubRouteResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HubRouteRepository extends JpaRepository<HubRoute, UUID>, HubRouteRepositoryCustom {

    Optional<HubRoute> findByStartHubId(UUID startHubId);


    @Modifying
    @Query("UPDATE HubRoute s SET s.deletedAt = :deletedAt, s.isDeleted = true WHERE s.id = :hubRouteId")
    void delete(@Param("hubRouteId") UUID hubRouteId, @Param("deletedAt") LocalDateTime deletedAt);
}