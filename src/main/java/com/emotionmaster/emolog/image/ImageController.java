package com.emotionmaster.emolog.image;

import com.emotionmaster.emolog.config.error.errorcode.DiaryErrorCode;
import com.emotionmaster.emolog.config.error.exception.DiaryException;
import com.emotionmaster.emolog.config.error.response.ApiErrorResponse;
import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.repository.DiaryRepository;
import com.emotionmaster.emolog.image.dto.ImageResponse;
import com.emotionmaster.emolog.image.dto.ImageRequest;
import com.emotionmaster.emolog.image.service.ImageService;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;
    private final DiaryRepository diaryRepository;
    private final UserService userService;


    @PostMapping("/api/image/upload")
    public String uploadImage(@RequestParam String imageUrl, Long diaryId) throws Exception {
        return imageService.saveImage(imageUrl, diaryId);
    }

    @PostMapping("/api/image/getS3ImageUrl")
    public String getS3ImageUrl(Long diaryId) {
        return imageService.getS3ImageUrl(diaryId);
    }

    @Transactional
    @PostMapping("/api/image/fetch-url")
    public ResponseEntity<ImageResponse> fetchImageUrl(ImageRequest imageRequest) {
        User user = userService.getCurrentUser();

        if (diaryRepository.findByDateAndUserId(imageRequest.getDate(), user.getId()).isPresent())
            throw new DiaryException(DiaryErrorCode.DIARY_DUPLICATED);

        Diary diary = diaryRepository.save(Diary.builder()
                .content(imageRequest.getContent())
                .date(imageRequest.getDate())
                .user(user)
                .build());
        try {
            //chatgpt 에 image URL 생성 요청
            String imageUrl = imageService.fetchImageUrlFromApi(imageRequest.getContent(), imageRequest.getHexacode());

            //생성된 image 저장
            String S3imageUrl = imageService.saveImage(imageUrl, diary.getId());
            return ResponseEntity.ok()
                    .body(new ImageResponse(S3imageUrl));
        } catch (IOException e) {
            log.error("Error in fetchImageUrl: ", e);
            throw new DiaryException(DiaryErrorCode.API_BAD_REQUEST);
        }  catch (Exception e) {
            log.error("Error in fetchImageUrl", e);
            throw new DiaryException(DiaryErrorCode.IMAGE_SAVE_ERROR);
        }
    }




}
