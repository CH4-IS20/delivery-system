package com.sparta.ch4.delivery.user.application.service;

import com.sparta.ch4.delivery.user.application.dto.LoginDto;
import com.sparta.ch4.delivery.user.application.dto.RegisterDto;
import com.sparta.ch4.delivery.user.domain.model.User;
import com.sparta.ch4.delivery.user.domain.repository.UserRepository;
import com.sparta.ch4.delivery.user.domain.service.UserDomainService;
import com.sparta.ch4.delivery.user.infrastructure.jwt.JwtTokenProvider;
import com.sparta.ch4.delivery.user.presentation.response.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDomainService userDomainService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserResponse register(RegisterDto dto) {
        return UserResponse.fromUserDto(userDomainService.createUser(dto));
    }

    @Transactional(readOnly = true)
    public UserResponse login(HttpServletResponse response, LoginDto dto) {
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new EntityNotFoundException("Username 에 해당하는 사용자가 없습니다."));
        checkPassword(dto.password(), user.getPassword());

        String token = jwtTokenProvider.createToken(user.getId(), user.getUsername(), user.getRole());
        final String BEARER_PREFIX = "Bearer ";
        response.setHeader("Authorization", BEARER_PREFIX + token);
        return UserResponse.fromUserEntity(user);
    }

    private void checkPassword(String inputPassword, String userPassword) {
        if (!passwordEncoder.matches(inputPassword, userPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
