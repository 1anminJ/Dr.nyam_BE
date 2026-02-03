package com.project.dr_nyam_be.service;

import com.project.dr_nyam_be.entity.Pin;
import com.project.dr_nyam_be.entity.ShareLink;
import com.project.dr_nyam_be.entity.User;
import com.project.dr_nyam_be.repository.ShareLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShareLinkService {

    private final ShareLinkRepository shareLinkRepository;
    private final UserService userService;
    private final PinService pinService;

    // 공유 링크 생성
    @Transactional
    public ShareLink createShareLink(Long userId, Integer expirationDays) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        ShareLink shareLink = new ShareLink();
        shareLink.setUser(user);

        // 만료일 설정 (null이면 영구 링크)
        if (expirationDays != null) {
            shareLink.setExpiredAt(LocalDateTime.now().plusDays(expirationDays));
        }

        return shareLinkRepository.save(shareLink);
    }

    // UUID로 공유 링크 조회
    public Optional<ShareLink> findByLinkUuid(String linkUuid) {
        return shareLinkRepository.findByLinkUuid(linkUuid);
    }

    // 공유 링크로 핀 목록 조회 (비회원도 접근 가능)
    public List<Pin> getPinsByShareLink(String linkUuid) {
        ShareLink shareLink = shareLinkRepository.findByLinkUuid(linkUuid)
                .orElseThrow(() -> new RuntimeException("공유 링크를 찾을 수 없습니다."));

        // 만료 체크
        if (shareLink.getExpiredAt() != null && shareLink.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("만료된 공유 링크입니다.");
        }

        return pinService.findByUserId(shareLink.getUser().getId());
    }

    // 사용자의 공유 링크 목록
    public List<ShareLink> findByUserId(Long userId) {
        return shareLinkRepository.findByUserId(userId);
    }

    // 공유 링크 삭제
    public void deleteByLinkUuid(String linkUuid) {
        shareLinkRepository.deleteById(linkUuid);
    }
}
