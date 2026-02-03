package com.project.dr_nyam_be.controller;

import com.project.dr_nyam_be.dto.*;
import com.project.dr_nyam_be.service.AuthService;
import com.project.dr_nyam_be.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth", description = "인증 관련 API (회원가입, 로그인)")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final KakaoService kakaoService;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    // ==================== 일반 로그인 ====================

    @Operation(summary = "회원가입", description = "이메일/비밀번호로 회원가입합니다.")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        try {
            TokenResponse response = authService.signUp(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "로그인", description = "이메일/비밀번호로 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            TokenResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "토큰 갱신", description = "Refresh Token으로 새로운 Access Token을 발급받습니다.")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        try {
            TokenResponse response = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==================== 내 정보 관리 ====================

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal Long userId) {
        try {
            UserInfoResponse response = authService.getMyInfo(userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "비밀번호 변경", description = "로그인한 사용자의 비밀번호를 변경합니다. (일반 로그인 사용자만 가능)")
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal Long userId,
            @RequestBody PasswordChangeRequest request) {
        try {
            authService.changePassword(userId, request);
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "회원 탈퇴", description = "로그인한 사용자의 계정을 삭제합니다.")
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal Long userId) {
        try {
            authService.deleteAccount(userId);
            return ResponseEntity.ok("계정이 성공적으로 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==================== 카카오 로그인 ====================

    @Operation(summary = "카카오 로그인 URL", description = "카카오 로그인 페이지 URL을 반환합니다.")
    @GetMapping("/kakao/url")
    public ResponseEntity<Map<String, String>> getKakaoLoginUrl() {
        String url = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectUri
                + "&response_type=code";

        return ResponseEntity.ok(Map.of("url", url));
    }

    @Operation(summary = "카카오 로그인", description = "카카오 인가 코드로 로그인/회원가입을 처리합니다.")
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginRequest request) {
        try {
            // 1. 카카오 토큰 받기
            String kakaoAccessToken = kakaoService.getAccessToken(request.getCode());

            // 2. 카카오 사용자 정보 가져오기
            Map<String, String> userInfo = kakaoService.getUserInfo(kakaoAccessToken);

            // 3. 로그인/회원가입 처리
            TokenResponse response = authService.kakaoLogin(
                    userInfo.get("id"),
                    userInfo.get("email"),
                    userInfo.get("nickname")
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "카카오 콜백", description = "카카오 로그인 후 리다이렉트되는 콜백 엔드포인트입니다. (프론트엔드에서 처리하는 것을 권장)")
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        try {
            String kakaoAccessToken = kakaoService.getAccessToken(code);
            Map<String, String> userInfo = kakaoService.getUserInfo(kakaoAccessToken);

            TokenResponse response = authService.kakaoLogin(
                    userInfo.get("id"),
                    userInfo.get("email"),
                    userInfo.get("nickname")
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
