package com.project.dr_nyam_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "내 정보 응답")
public class UserInfoResponse {

    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "닉네임", example = "맛집헌터")
    private String nickname;

    @Schema(description = "로그인 제공자", example = "LOCAL")
    private String provider;

    @Schema(description = "역할", example = "USER")
    private String role;

    @Schema(description = "가입일")
    private LocalDateTime createdAt;
}
