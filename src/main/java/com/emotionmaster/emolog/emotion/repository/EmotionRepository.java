package com.emotionmaster.emolog.emotion.repository;

import com.emotionmaster.emolog.emotion.domain.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {
}
