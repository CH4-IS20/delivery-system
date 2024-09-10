package com.sparta.ch4.delivery.user.application.service;

import com.sparta.ch4.delivery.user.application.dto.RegisterDto;
import com.sparta.ch4.delivery.user.domain.service.UserDomainService;
import com.sparta.ch4.delivery.user.presentation.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDomainService userDomainService;

    @Transactional
    public UserResponse register(RegisterDto dto) {
        return UserResponse.fromUserDto(userDomainService.createUser(dto));
    }

}
