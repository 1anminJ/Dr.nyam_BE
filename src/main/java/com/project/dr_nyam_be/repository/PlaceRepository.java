package com.project.dr_nyam_be.repository;

import com.project.dr_nyam_be.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByNaverPlaceId(String naverPlaceId);

    boolean existsByNaverPlaceId(String naverPlaceId);
}
