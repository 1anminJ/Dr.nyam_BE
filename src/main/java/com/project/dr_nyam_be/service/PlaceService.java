package com.project.dr_nyam_be.service;

import com.project.dr_nyam_be.entity.Place;
import com.project.dr_nyam_be.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    // ID로 장소 조회
    public Optional<Place> findById(Long id) {
        return placeRepository.findById(id);
    }

    // 네이버 장소 ID로 조회
    public Optional<Place> findByNaverPlaceId(String naverPlaceId) {
        return placeRepository.findByNaverPlaceId(naverPlaceId);
    }

    // 장소 저장 (이미 있으면 기존 장소 반환)
    public Place saveOrGet(Place place) {
        Optional<Place> existing = placeRepository.findByNaverPlaceId(place.getNaverPlaceId());
        if (existing.isPresent()) {
            return existing.get();
        }
        return placeRepository.save(place);
    }
}
