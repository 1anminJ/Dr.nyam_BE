package com.project.dr_nyam_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "핀 저장 요청")
public class PinCreateRequest {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "장소 정보")
    private PlaceRequest place;

    @Schema(description = "별점 (1~5)", example = "5", minimum = "1", maximum = "5")
    private Integer rating;

    @Schema(description = "한줄평", example = "분위기 좋고 맛있어요!")
    private String comment;

    @Schema(description = "태그 (쉼표로 구분)", example = "데이트,분위기좋은,가성비")
    private String tags;
}
