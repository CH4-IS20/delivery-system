package com.sparta.ch4.delivery.user.presentation.controller;

import com.sparta.ch4.delivery.user.application.service.UserService;
import com.sparta.ch4.delivery.user.domain.type.UserSearchType;
import com.sparta.ch4.delivery.user.presentation.request.UserUpdateRequest;
import com.sparta.ch4.delivery.user.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.user.presentation.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public CommonResponse<UserResponse> getUser(@PathVariable Long userId) {
        return CommonResponse.success(userService.getUserById(userId));
    }

    @GetMapping
    public CommonResponse<Page<UserResponse>> getUsers(
            @RequestParam(required = false, name = "searchType") UserSearchType searchType,
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return CommonResponse.success(userService.getUsers(searchType, searchValue, pageable));
    }

    @PutMapping("/{userId}")
    public CommonResponse<UserResponse> updateUser(
            @RequestHeader("X-UserId") String updatedBy,
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequest updateUserRequest) {
        return CommonResponse.success(userService.updateUser(userId, updateUserRequest.toDto(updatedBy)));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(
            @RequestHeader("X-UserId") String deleteBy,
            @PathVariable Long userId) {
        userService.deleteUser(userId, deleteBy);
    }

}