package com.project.dr_nyam_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 생성 요청")
public class UserCreateRequest {

    @Schema(description = "이메일", example = "test@example.com")
    private String email;

    @Schema(description = "닉네임", example = "맛집헌터")
    private String nickname;

    @Schema(description = "소셜 로그인 제공자", example = "KAKAO", allowableValues = {"KAKAO", "NAVER"})
    private String provider;
}
