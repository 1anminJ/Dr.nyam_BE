package com.project.dr_nyam_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "핀 수정 요청")
public class PinUpdateRequest {

    @Schema(description = "별점 (1~5)", example = "4", minimum = "1", maximum = "5")
    private Integer rating;

    @Schema(description = "한줄평", example = "재방문! 역시 맛있네요")
    private String comment;

    @Schema(description = "태그 (쉼표로 구분)", example = "재방문,추천")
    private String tags;
}
