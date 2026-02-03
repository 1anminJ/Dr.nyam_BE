package com.project.dr_nyam_be.repository;

import com.project.dr_nyam_be.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {

    // 특정 사용자의 모든 핀 조회
    List<Pin> findByUserId(Long userId);

    // 특정 사용자가 특정 장소에 핀을 저장했는지 확인
    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);
}
