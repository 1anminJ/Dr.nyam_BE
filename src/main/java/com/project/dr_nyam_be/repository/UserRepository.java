package com.project.dr_nyam_be.repository;

import com.project.dr_nyam_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // 소셜 로그인용: provider와 providerId로 사용자 조회
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
