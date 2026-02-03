package com.project.dr_nyam_be.repository;

import com.project.dr_nyam_be.entity.ShareLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShareLinkRepository extends JpaRepository<ShareLink, String> {

    // UUID로 공유 링크 조회
    Optional<ShareLink> findByLinkUuid(String linkUuid);

    // 특정 사용자의 공유 링크 목록
    java.util.List<ShareLink> findByUserId(Long userId);
}
