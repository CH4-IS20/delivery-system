package com.sparta.ch4.delivery.user.domain.service;

import com.sparta.ch4.delivery.user.application.dto.RegisterDto;
import com.sparta.ch4.delivery.user.application.dto.UserDto;
import com.sparta.ch4.delivery.user.application.dto.UserPageDto;
import com.sparta.ch4.delivery.user.application.dto.UserUpdateDto;
import com.sparta.ch4.delivery.user.domain.exception.ApplicationException;
import com.sparta.ch4.delivery.user.domain.exception.ErrorCode;
import com.sparta.ch4.delivery.user.domain.model.User;
import com.sparta.ch4.delivery.user.domain.repository.UserPageRepository;
import com.sparta.ch4.delivery.user.domain.repository.UserRepository;
import com.sparta.ch4.delivery.user.domain.type.UserSearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    private final UserPageRepository userPageRepository;

    private final PasswordEncoder passwordEncoder;

    public UserDto createUser(RegisterDto dto) {
        checkDuplicate(dto.username(), dto.email());

        String encodedPassword = passwordEncoder.encode(dto.password());
        User user = userRepository.save(dto.toEntity(encodedPassword));
        user.setCreatedBy(String.valueOf(user.getId()));
        return UserDto.from(userRepository.save(user));
    }

    public UserDto getUserById(Long userId) {
       return userRepository.findById(userId).map(UserDto::from)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    public Page<UserPageDto> getUsers(UserSearchType searchType, String searchValue, Pageable pageable) {
        return userPageRepository.searchUser(searchType, searchValue, pageable);
    }

    public UserDto updateUser(Long userId, UserUpdateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        String password = passwordEncoder.encode(dto.password());
        user.update(dto, password);
        return UserDto.from(userRepository.save(user));
    }

    public void deleteUserById(Long userId, String deleteBy) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        user.delete(deleteBy);
        userRepository.save(user);
    }

    private void checkDuplicate(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new ApplicationException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(email)) {
            throw new ApplicationException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

}
