package com.emotionmaster.emolog.image.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Builder
    public Image (String imageUrl){
        this.imageUrl = imageUrl;
    }
}
