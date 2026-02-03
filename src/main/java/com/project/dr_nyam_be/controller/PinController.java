package com.project.dr_nyam_be.controller;

import com.project.dr_nyam_be.dto.PinCreateRequest;
import com.project.dr_nyam_be.dto.PinUpdateRequest;
import com.project.dr_nyam_be.entity.Pin;
import com.project.dr_nyam_be.entity.Place;
import com.project.dr_nyam_be.service.PinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pin", description = "맛집 핀 관련 API")
@RestController
@RequestMapping("/api/pins")
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;

    @Operation(summary = "사용자별 핀 목록 조회", description = "특정 사용자가 저장한 모든 맛집 핀 목록을 조회합니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Pin>> getPinsByUserId(
            @Parameter(description = "사용자 ID", example = "1") @PathVariable Long userId) {
        return ResponseEntity.ok(pinService.findByUserId(userId));
    }

    @Operation(summary = "핀 상세 조회", description = "핀 ID로 특정 핀의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<Pin> getPinById(
            @Parameter(description = "핀 ID", example = "1") @PathVariable Long id) {
        return pinService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "핀 저장", description = "새로운 맛집 핀을 저장합니다. 장소 정보, 별점, 코멘트, 태그를 포함합니다.")
    @PostMapping
    public ResponseEntity<?> createPin(@RequestBody PinCreateRequest request) {
        try {
            Place place = new Place();
            place.setNaverPlaceId(request.getPlace().getNaverPlaceId());
            place.setName(request.getPlace().getName());
            place.setCategory(request.getPlace().getCategory());
            place.setLatitude(request.getPlace().getLatitude());
            place.setLongitude(request.getPlace().getLongitude());

            Pin pin = pinService.save(request.getUserId(), place, request.getRating(), request.getComment(), request.getTags());
            return ResponseEntity.ok(pin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "핀 수정", description = "기존 핀의 별점, 코멘트, 태그를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePin(
            @Parameter(description = "핀 ID", example = "1") @PathVariable Long id,
            @RequestBody PinUpdateRequest request) {
        try {
            Pin pin = pinService.update(id, request.getRating(), request.getComment(), request.getTags());
            return ResponseEntity.ok(pin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "핀 삭제", description = "핀 ID로 특정 핀을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePin(
            @Parameter(description = "핀 ID", example = "1") @PathVariable Long id) {
        pinService.deleteById(id);
        return ResponseEntity.ok("핀이 성공적으로 삭제되었습니다. (ID: " + id + ")");
    }
}
