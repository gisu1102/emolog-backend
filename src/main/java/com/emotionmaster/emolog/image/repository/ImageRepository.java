package com.emotionmaster.emolog.image.repository;

import com.emotionmaster.emolog.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByDiaryId(Long diaryId);
}
