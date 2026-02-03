package com.project.dr_nyam_be.controller;

import com.project.dr_nyam_be.dto.ShareLinkCreateRequest;
import com.project.dr_nyam_be.entity.Pin;
import com.project.dr_nyam_be.entity.ShareLink;
import com.project.dr_nyam_be.service.ShareLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Share", description = "공유 링크 관련 API")
@RestController
@RequestMapping("/api/share")
@RequiredArgsConstructor
public class ShareController {

    private final ShareLinkService shareLinkService;

    @Operation(summary = "공유 링크 생성", description = "내 맛집 지도를 공유할 수 있는 링크를 생성합니다. 만료일 설정 가능.")
    @PostMapping
    public ResponseEntity<?> createShareLink(@RequestBody ShareLinkCreateRequest request) {
        try {
            ShareLink shareLink = shareLinkService.createShareLink(request.getUserId(), request.getExpirationDays());
            return ResponseEntity.ok(shareLink);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "공유된 맛집 목록 조회", description = "공유 링크 UUID로 해당 사용자의 맛집 핀 목록을 조회합니다. (비회원 접근 가능)")
    @GetMapping("/{linkUuid}")
    public ResponseEntity<?> getSharedPins(
            @Parameter(description = "공유 링크 UUID", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable String linkUuid) {
        try {
            List<Pin> pins = shareLinkService.getPinsByShareLink(linkUuid);
            return ResponseEntity.ok(pins);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "내 공유 링크 목록", description = "특정 사용자가 생성한 모든 공유 링크를 조회합니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ShareLink>> getShareLinksByUserId(
            @Parameter(description = "사용자 ID", example = "1") @PathVariable Long userId) {
        return ResponseEntity.ok(shareLinkService.findByUserId(userId));
    }

    @Operation(summary = "공유 링크 삭제", description = "공유 링크를 삭제합니다.")
    @DeleteMapping("/{linkUuid}")
    public ResponseEntity<String> deleteShareLink(
            @Parameter(description = "공유 링크 UUID", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable String linkUuid) {
        shareLinkService.deleteByLinkUuid(linkUuid);
        return ResponseEntity.ok("공유 링크가 성공적으로 삭제되었습니다. (UUID: " + linkUuid + ")");
    }
}
