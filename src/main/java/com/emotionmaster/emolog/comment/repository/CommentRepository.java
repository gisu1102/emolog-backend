package com.emotionmaster.emolog.comment.repository;

import com.emotionmaster.emolog.comment.domain.Comment;
import com.emotionmaster.emolog.emotion.domain.EmotionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.type = :emotionType ORDER BY RAND() LIMIT 1")
    Comment findCommentByEmotionType(EmotionType emotionType);

}
