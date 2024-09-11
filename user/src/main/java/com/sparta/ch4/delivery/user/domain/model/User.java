package com.sparta.ch4.delivery.user.domain.model;

import com.sparta.ch4.delivery.user.application.dto.UserUpdateDto;
import com.sparta.ch4.delivery.user.domain.type.UserRole;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "p_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "hub_id", nullable = true)
    private UUID hubId;

    @Column(name = "company_id", nullable = true)
    private UUID companyId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "slack_id", nullable = false)
    private String slackId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void update(UserUpdateDto dto, String password) {
        this.username = dto.username();
        this.email = dto.email();
        this.role = dto.role();
        this.password = password;
        this.hubId = dto.hubId();
        this.companyId = dto.companyId();
        this.slackId = dto.slackId();
        this.setUpdatedBy(dto.updatedBy());
    }

    public void delete(String deletedBy) {
        this.setDeletedBy(deletedBy);
        this.setDeletedAt(LocalDateTime.now());
        this.setIsDeleted(true);
    }

}

