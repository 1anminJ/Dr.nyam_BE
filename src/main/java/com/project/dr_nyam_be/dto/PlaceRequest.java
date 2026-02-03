package com.project.dr_nyam_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "장소 정보")
public class PlaceRequest {

    @Schema(description = "네이버 장소 ID", example = "1234567890")
    private String naverPlaceId;

    @Schema(description = "장소 이름", example = "을지로 골목식당")
    private String name;

    @Schema(description = "카테고리", example = "한식")
    private String category;

    @Schema(description = "위도", example = "37.5665")
    private Double latitude;

    @Schema(description = "경도", example = "126.9780")
    private Double longitude;
}
