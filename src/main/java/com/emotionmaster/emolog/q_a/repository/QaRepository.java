package com.emotionmaster.emolog.q_a.repository;

import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.q_a.domain.Q_A;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QaRepository extends JpaRepository<Q_A, Long> {
}
