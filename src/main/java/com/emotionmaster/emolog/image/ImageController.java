package com.emotionmaster.emolog.image;

import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    //테스트용도
    @PostMapping("/api/image/upload")
    public String uploadImage(@RequestParam String imageUrl, Long diaryId) throws Exception {
        return imageService.saveImage(imageUrl, diaryId);
    }
    @PostMapping("/api/image/getImageUrl")
    public String getImageUrl(Long diaryId) {
        return imageService.getImageUrl(diaryId);
    }




}
