package com.sparta.ch4.delivery.user.application.service;

import com.sparta.ch4.delivery.user.application.dto.UserUpdateDto;
import com.sparta.ch4.delivery.user.domain.service.UserDomainService;
import com.sparta.ch4.delivery.user.domain.type.UserSearchType;
import com.sparta.ch4.delivery.user.presentation.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDomainService userDomainService;

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        return UserResponse.fromUserDto(userDomainService.getUserById(userId));
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsers(UserSearchType searchType, String searchValue, Pageable pageable) {
        return userDomainService.getUsers(searchType, searchValue, pageable).map(UserResponse::fromUserPageDto);
    }

    @Transactional
    public UserResponse updateUser(Long userId, UserUpdateDto dto) {
        return UserResponse.fromUserDto(userDomainService.updateUser(userId, dto));
    }

    @Transactional
    public void deleteUser(Long userId, String deletedBy) {
        userDomainService.deleteUserById(userId, deletedBy);
    }

}
