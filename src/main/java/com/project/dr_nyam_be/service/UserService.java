package com.project.dr_nyam_be.service;

import com.project.dr_nyam_be.entity.User;
import com.project.dr_nyam_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 모든 사용자 조회
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // ID로 사용자 조회
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // 이메일로 사용자 조회
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 사용자 저장
    public User save(User user) {
        return userRepository.save(user);
    }

    // 사용자 삭제
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
