package com.emotionmaster.emolog.diary.repository;

import com.emotionmaster.emolog.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    // 해당 유저의 해당 월 일기 개수 조회 쿼리문
    @Query(value = "SELECT COUNT(*) FROM diary WHERE user_id = :userId AND YEAR(diary_date) = YEAR(CURDATE()) AND MONTH(diary_date) = MONTH(CURDATE())",
            nativeQuery = true)
    long countDiariesForUserThisMonth(@Param("userId") Long userId);
}
