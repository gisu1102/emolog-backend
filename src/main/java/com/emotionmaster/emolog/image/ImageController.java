package com.emotionmaster.emolog.image;

import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;


    @PostMapping("/api/image/upload")
    public String uploadImage(@RequestParam String imageUrl, Long diaryId) throws Exception {
        return imageService.saveImage(imageUrl, diaryId);
    }

    @PostMapping("/api/image/getS3ImageUrl")
    public String getS3ImageUrl(Long diaryId) {
        return imageService.getS3ImageUrl(diaryId);
    }

    @PostMapping("/api/image/fetch-url")
    public ResponseEntity<String> fetchImageUrl(@RequestParam String content, @RequestParam String hexacode, Long diaryId) {
        try {
            //chatgpt 에 image URL 생성 요청
            String imageUrl = imageService.fetchImageUrlFromApi(content, hexacode);

            //생성된 image 저장
            String S3imageUrl = imageService.saveImage(imageUrl, diaryId);
            return ResponseEntity.ok(S3imageUrl);
        } catch (IOException e) {
            log.error("Error in fetchImageUrl: ", e);
            return ResponseEntity.status(403).body("Image URL is invalid or has expired");
        }  catch (Exception e) {
            log.error("Error in fetchImageUrl", e);
            return ResponseEntity.status(500).body("Error /fetch-url");
        }
    }




}
