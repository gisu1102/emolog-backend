package com.emotionmaster.emolog.comment.service;

import com.emotionmaster.emolog.comment.domain.Comment;
import com.emotionmaster.emolog.comment.repository.CommentRepository;
import com.emotionmaster.emolog.emotion.domain.EmotionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    public Comment getComment(EmotionType emotionType) {
        return commentRepository.findCommentByEmotionType(emotionType);
    }
}
