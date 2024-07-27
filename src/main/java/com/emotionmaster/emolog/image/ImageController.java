package com.emotionmaster.emolog.image;

import com.emotionmaster.emolog.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public String uploadImage(@RequestParam String imageUrl) throws Exception {
        return imageService.saveImage(imageUrl);
    }




}
