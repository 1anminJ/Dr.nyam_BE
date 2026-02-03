package com.project.dr_nyam_be.service;

import com.project.dr_nyam_be.entity.Pin;
import com.project.dr_nyam_be.entity.Place;
import com.project.dr_nyam_be.entity.User;
import com.project.dr_nyam_be.repository.PinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PinService {

    private final PinRepository pinRepository;
    private final UserService userService;
    private final PlaceService placeService;

    // 특정 사용자의 모든 핀 조회
    public List<Pin> findByUserId(Long userId) {
        return pinRepository.findByUserId(userId);
    }

    // 핀 ID로 조회
    public Optional<Pin> findById(Long id) {
        return pinRepository.findById(id);
    }

    // 핀 저장
    @Transactional
    public Pin save(Long userId, Place placeInfo, Integer rating, String comment, String tags) {
        // 1. 사용자 조회
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 2. 장소 저장 또는 기존 장소 조회
        Place place = placeService.saveOrGet(placeInfo);

        // 3. 중복 체크
        if (pinRepository.existsByUserIdAndPlaceId(userId, place.getId())) {
            throw new RuntimeException("이미 저장한 장소입니다.");
        }

        // 4. 핀 생성 및 저장
        Pin pin = new Pin();
        pin.setUser(user);
        pin.setPlace(place);
        pin.setRating(rating);
        pin.setComment(comment);
        pin.setTags(tags);

        return pinRepository.save(pin);
    }

    // 핀 수정
    @Transactional
    public Pin update(Long pinId, Integer rating, String comment, String tags) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(() -> new RuntimeException("핀을 찾을 수 없습니다."));

        pin.setRating(rating);
        pin.setComment(comment);
        pin.setTags(tags);

        return pinRepository.save(pin);
    }

    // 핀 삭제
    public void deleteById(Long id) {
        pinRepository.deleteById(id);
    }
}
