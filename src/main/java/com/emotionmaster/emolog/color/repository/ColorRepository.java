package com.emotionmaster.emolog.color.repository;

import com.emotionmaster.emolog.color.domain.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
}
