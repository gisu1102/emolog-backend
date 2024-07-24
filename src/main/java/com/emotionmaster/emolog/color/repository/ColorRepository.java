package com.emotionmaster.emolog.color.repository;

import com.emotionmaster.emolog.color.domain.Color;
import com.emotionmaster.emolog.color.dto.response.ColorAndDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {

    @Query("SELECT new com.emotionmaster.emolog.color.dto.response.ColorAndDate(c.diary.date, c.hexa, c.diary.dayOfWeek) FROM Color c " +
            "WHERE MONTH(c.diary.date) = :month AND c.diary.user.id = :id")
    List<ColorAndDate> findColorAndDateByMonthAndUserId(@Param("month") int month, @Param("id") long id);

    @Query(value = "SELECT new com.emotionmaster.emolog.color.dto.response.ColorAndDate(c.diary.date, c.hexa, c.diary.dayOfWeek) FROM Color c " +
            "WHERE MONTH(c.diary.date) = :month AND c.diary.week = :week AND c.diary.user.id = :id")
    List<ColorAndDate> findAllByMonthAndWeekAndUserId(@Param("month") int month, @Param("week") int week, @Param("id") long id);
}
