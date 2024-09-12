package com.sparta.ch4.delivery.user.domain.repository;

import com.sparta.ch4.delivery.user.domain.model.Slack;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackRepository extends JpaRepository<Slack, UUID> {

}
