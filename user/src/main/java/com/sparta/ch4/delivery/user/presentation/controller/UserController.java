package com.sparta.ch4.delivery.user.presentation.controller;

import com.sparta.ch4.delivery.user.application.service.UserService;
import com.sparta.ch4.delivery.user.domain.type.UserSearchType;
import com.sparta.ch4.delivery.user.presentation.request.UserUpdateRequest;
import com.sparta.ch4.delivery.user.presentation.response.CommonResponse;
import com.sparta.ch4.delivery.user.presentation.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "사용자 관리를 위한 API입니다.")
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 ID로 사용자 조회", description = "특정 사용자 ID를 사용하여 사용자의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 정보가 성공적으로 조회되었습니다.")
    @ApiResponse(responseCode = "404", description = "ID 에 해당하는 사용자가 존재하지 않습니다.")
    @GetMapping("/{userId}")
    public CommonResponse<UserResponse> getUser(
            @Parameter(description = "조회할 사용자 ID") @PathVariable Long userId) {
        return CommonResponse.success(userService.getUserById(userId));
    }

    @Operation(summary = "사용자 목록 조회", description = "옵션으로 검색 조건을 사용하여 사용자 목록을 페이지로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 목록이 성공적으로 조회되었습니다.")
    @GetMapping
    public CommonResponse<Page<UserResponse>> getUsers(
            @Parameter(description = "검색할 유형", example = "USERNAME") @RequestParam(required = false, name = "searchType") UserSearchType searchType,
            @Parameter(description = "검색할 값") @RequestParam(required = false, name = "searchValue") String searchValue,
            @Parameter(description = "페이지네이션 및 정렬 정보") @PageableDefault(
                    size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return CommonResponse.success(userService.getUsers(searchType, searchValue, pageable));
    }

    @Operation(summary = "사용자 정보 수정", description = "특정 ID의 사용자의 정보를 업데이트합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 정보가 성공적으로 업데이트되었습니다.")
    @ApiResponse(responseCode = "404", description = "ID 에 해당하는 사용자가 존재하지 않습니다.")
    @PutMapping("/{userId}")
    public CommonResponse<UserResponse> updateUser(
            @Parameter(description = "수정 작업을 수행하는 사용자 ID") @RequestHeader("X-UserId") String updatedBy,
            @Parameter(description = "수정할 사용자 ID") @PathVariable Long userId,
            @Parameter(description = "수정할 사용자 정보 요청 데이터") @Valid @RequestBody UserUpdateRequest updateUserRequest) {
        return CommonResponse.success(userService.updateUser(userId, updateUserRequest.toDto(updatedBy)));
    }

    @Operation(summary = "사용자 삭제", description = "특정 ID의 사용자를 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "사용자가 성공적으로 삭제되었습니다.")
    @ApiResponse(responseCode = "404", description = "ID 에 해당하는 사용자가 존재하지 않습니다.")
    @DeleteMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(
            @Parameter(description = "삭제 작업을 수행하는 사용자 ID") @RequestHeader("X-UserId") String deleteBy,
            @Parameter(description = "삭제할 사용자 ID") @PathVariable Long userId) {
        userService.deleteUser(userId, deleteBy);
    }

}
