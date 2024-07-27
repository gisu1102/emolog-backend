package com.emotionmaster.emolog.image.domain;

import com.emotionmaster.emolog.config.BaseEntity;
import com.emotionmaster.emolog.diary.domain.Diary;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Getter
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "imageUrl")
    private String imageUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Builder
    public Image (String imageUrl, Diary diary){

        this.imageUrl = imageUrl;
        this.diary = diary;
    }
}
