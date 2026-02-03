package com.project.dr_nyam_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "공유 링크 생성 요청")
public class ShareLinkCreateRequest {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "만료일 (일 단위, 미입력시 무기한)", example = "7")
    private Integer expirationDays;
}
