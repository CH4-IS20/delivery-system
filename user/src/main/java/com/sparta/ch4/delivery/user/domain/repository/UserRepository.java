package com.sparta.ch4.delivery.user.domain.repository;

import com.sparta.ch4.delivery.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
