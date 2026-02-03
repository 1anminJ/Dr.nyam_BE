package com.project.dr_nyam_be.controller;

import com.project.dr_nyam_be.dto.UserCreateRequest;
import com.project.dr_nyam_be.entity.User;
import com.project.dr_nyam_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "모든 사용자 조회", description = "등록된 모든 사용자 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "사용자 조회", description = "ID로 특정 사용자를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "사용자 ID", example = "1") @PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다. (테스트용 - 나중에 OAuth로 대체)")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setProvider(request.getProvider());
        return ResponseEntity.ok(userService.save(user));
    }

    @Operation(summary = "사용자 삭제", description = "ID로 특정 사용자를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "사용자 ID", example = "1") @PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다. (ID: " + id + ")");
    }
}
