package com.example.hub.domain.repository;

import com.example.hub.domain.model.Hub;
import com.example.hub.domain.model.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HubRepository extends JpaRepository<Hub, UUID>,HubRepositoryCustom {

    // TODO :: User 연동시 deleteBy도 넣어줌
//    @Modifying
//    @Query("UPDATE Hub s SET s.deletedAt = :deletedAt , s.isDeleted = true, s.deletedBy = :deletedBy WHERE s.id = :hubId")
//    void delete(@Param("hubId") UUID hubId, @Param("deletedBy")String deletedBy);

    @Modifying
    @Query("UPDATE Hub s SET s.deletedAt = :deletedAt, s.isDeleted = true , s.deletedBy = :deletedBy WHERE s.id = :hubId")
    void delete(@Param("hubId") UUID hubId, @Param("deletedAt")LocalDateTime deletedAt, @Param("deletedBy")String deletedBy);

    Optional<Hub> findByName(String name);

    @Query("SELECT h FROM Hub h JOIN h.deliveryManagers d WHERE h.name = :startHubName AND d.status = false ")
    Optional<Hub> findByNameDelivery(@Param("startHubName") String startHubName);
}
