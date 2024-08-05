package com.emotionmaster.emolog.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.repository.DiaryRepository;
import com.emotionmaster.emolog.image.domain.Image;
import com.emotionmaster.emolog.image.repository.ImageRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final AmazonS3 amazonS3Client;
    private final ImageRepository imageRepository;
    private final DiaryRepository diaryRepository;

    private final RestTemplate restTemplate;
    //Object <-> JSON 변환
    private final ObjectMapper objectMapper;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    // 해당 일기와 연결된 이미지 가져오기
    public String getS3ImageUrl(Long diaryId) {
        // 일기 ID로 이미지 조회
        Image image = imageRepository.findByDiaryId(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found for the given diaryId"));

        //url 에 해당하는 이미지 불러오기 ( 프론트에서 담당 )
        //S3Object s3Object = amazonS3Client.getObject(bucket, s3Url);

        return image.getImageUrl();
    }


    public String saveImage (String imageUrl , Long diaryId ) throws Exception {
        //url 로 부터 이미지 byte 변환해서 불러오기
        byte[] imageBytes = downloadImageFromUrl( imageUrl );
        //S3에 이미지 저장
        String s3Url = uploadToS3( imageBytes );


        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("Diary not found"));

        //db에 s3Url 저장하기
        Image image = Image.builder()
                .imageUrl(s3Url)
                .diary(diary)
                .build();
        imageRepository.save(image);

        return s3Url;
    }


    //url -> image 다운로드
    private byte[] downloadImageFromUrl (String imageUrl) throws Exception{
       try
               (InputStream inputStream = new URL(imageUrl).openStream()) {
           return inputStream.readAllBytes();
       }
    }

    //S3 에 업로드
    private String uploadToS3(byte[] imageBytes) {
        //업로드할 이미지의 S3 Key 값 생성
        //UUID.radomUUID -> 고유 식별자 생성
        //ex) images/3j32j2k3......png
        String key = "images/" + UUID.randomUUID() + ".png";

        //이미지 바이트 배열과 메타 데이터 사용해서 S3 에 업로드
        //S3 객체(파일) 에 대한 사용자정의(amazonS3) 추가 정보 포함 (파일 크기, 유형, 버켓,키)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/png");
        //브라우저에 바로 표시 되도록 - 설정x 시 다운로드
        metadata.setContentDisposition("inline");
        metadata.setContentLength(imageBytes.length);
        amazonS3Client.putObject(bucket, key, new ByteArrayInputStream(imageBytes), metadata);

        return amazonS3Client.getUrl(bucket, key).toString();
    }


    //ChatGPT Image 생성 API 호출 및 이미지 Url 받기
    public String fetchImageUrlFromApi(String content) throws Exception {
        String apiUrl = "http://3.34.51.210:8000/api/ai/image";
        log.info("apiUrl : " + apiUrl);

        // 요청 바디 객체 구성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("content", content);

        // 요청 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 객체 생성
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // API 호출 및 응답 받기
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, entity, String.class);
        String response = responseEntity.getBody();
        log.info("response : " + response);

        // 응답에서 URL 추출
        // 객체 - json 변환
        JsonNode jsonNode = objectMapper.readTree(response);
        String imageUrl = jsonNode.get("url").asText();
        log.info("imageUrl : " + imageUrl);
        return imageUrl;
    }



}
