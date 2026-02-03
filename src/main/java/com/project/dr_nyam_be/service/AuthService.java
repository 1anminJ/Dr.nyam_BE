package com.project.dr_nyam_be.service;

import com.project.dr_nyam_be.dto.*;
import com.project.dr_nyam_be.entity.User;
import com.project.dr_nyam_be.repository.UserRepository;
import com.project.dr_nyam_be.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 일반 회원가입
    @Transactional
    public TokenResponse signUp(SignUpRequest request) {
        // 이메일 중복 검사
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        // 사용자 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .provider("LOCAL")
                .role("USER")
                .build();

        User savedUser = userRepository.save(user);

        return createTokenResponse(savedUser);
    }

    // 일반 로그인
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        // LOCAL 가입자인지 확인
        if (!"LOCAL".equals(user.getProvider())) {
            throw new RuntimeException("소셜 로그인으로 가입된 계정입니다. " + user.getProvider() + " 로그인을 이용해주세요.");
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return createTokenResponse(user);
    }

    // 카카오 로그인/회원가입
    @Transactional
    public TokenResponse kakaoLogin(String kakaoId, String email, String nickname) {
        // 기존 회원인지 확인 (카카오 ID로)
        User user = userRepository.findByProviderAndProviderId("KAKAO", kakaoId)
                .orElseGet(() -> {
                    // 이메일로 기존 회원 확인
                    if (email != null && userRepository.findByEmail(email).isPresent()) {
                        throw new RuntimeException("이미 다른 방식으로 가입된 이메일입니다.");
                    }

                    // 신규 회원 생성
                    User newUser = User.builder()
                            .email(email != null ? email : "kakao_" + kakaoId + "@drnyam.com")
                            .nickname(nickname != null ? nickname : "카카오유저")
                            .provider("KAKAO")
                            .providerId(kakaoId)
                            .role("USER")
                            .build();

                    return userRepository.save(newUser);
                });

        return createTokenResponse(user);
    }

    // 토큰 갱신
    public TokenResponse refreshToken(String refreshToken) {
        // 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return createTokenResponse(user);
    }

    // 내 정보 조회
    public UserInfoResponse getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .provider(user.getProvider())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(Long userId, PasswordChangeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // LOCAL 가입자만 비밀번호 변경 가능
        if (!"LOCAL".equals(user.getProvider())) {
            throw new RuntimeException("소셜 로그인 사용자는 비밀번호를 변경할 수 없습니다.");
        }

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 설정
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        userRepository.delete(user);
    }

    // 토큰 응답 생성
    private TokenResponse createTokenResponse(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
