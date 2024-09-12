package com.sparta.ch4.delivery.user.application.dto;

import com.sparta.ch4.delivery.user.domain.type.UserRole;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserUpdateDto(
        String username,
        String email,
        String password,
        UserRole role,
        UUID hubId,
        UUID companyId,
        String updatedBy
) {

}
