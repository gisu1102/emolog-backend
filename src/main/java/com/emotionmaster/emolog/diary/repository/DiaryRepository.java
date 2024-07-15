package com.emotionmaster.emolog.diary.repository;

import com.emotionmaster.emolog.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("SELECT d FROM Diary d WHERE MONTH(d.date) =:month")
    List<Diary> findColorByMonth(int month);
}
