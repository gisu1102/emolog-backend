package com.emotionmaster.emolog.user.repository;

import com.emotionmaster.emolog.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    //email + provider 사용자 검색
    Optional<User> findByEmailAndOauthType(String email, String OauthType);
}
