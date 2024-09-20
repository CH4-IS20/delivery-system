package com.sparta.ch4.delivery.user.domain.repository;

import com.sparta.ch4.delivery.user.application.dto.UserPageDto;
import com.sparta.ch4.delivery.user.domain.type.UserSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPageRepository {
    Page<UserPageDto> searchUser(UserSearchType searchType, String searchValue, Pageable pageable);
}
