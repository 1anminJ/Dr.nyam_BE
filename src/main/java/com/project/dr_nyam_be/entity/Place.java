package com.project.dr_nyam_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "places")
@Getter
@Setter
@NoArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @Column(name = "naver_place_id", nullable = false, unique = true, length = 100)
    private String naverPlaceId;  // 네이버 지도 장소 ID (중복 저장 방지)

    @Column(nullable = false, length = 200)
    private String name;  // 가게 이름

    @Column(length = 100)
    private String category;  // 카테고리 (한식, 중식 등)

    @Column(nullable = false)
    private Double latitude;  // 위도

    @Column(nullable = false)
    private Double longitude;  // 경도
}
