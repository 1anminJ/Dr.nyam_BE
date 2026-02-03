package com.project.dr_nyam_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "카카오 로그인 요청")
public class KakaoLoginRequest {

    @Schema(description = "카카오 인가 코드", example = "authorization_code_from_kakao")
    private String code;
}
