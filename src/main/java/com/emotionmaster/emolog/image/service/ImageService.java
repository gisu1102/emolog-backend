package com.emotionmaster.emolog.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.emotionmaster.emolog.image.domain.Image;
import com.emotionmaster.emolog.image.repository.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Service
@NoArgsConstructor
@AllArgsConstructor

public class ImageService {

    @Autowired
    private AmazonS3 amazonS3Client;

    //Repository 왜 자동 빈주입 X???
    @Autowired
    private ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveImage (String imageUrl ) throws Exception {

        //url 로 부터 이미지 byte 변환해서 불러오기
        byte[] imageBytes = downloadImageFromUrl( imageUrl );

        //S3에 이미지 저장
        String s3Url = uploadToS3( imageBytes );

        //db에 s3Url 저장하기
        Image image = Image.builder()
                .imageUrl(s3Url)
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
        metadata.setContentLength(imageBytes.length);
        amazonS3Client.putObject(bucket, key, new ByteArrayInputStream(imageBytes), metadata);

        return amazonS3Client.getUrl(bucket, key).toString();
    }



}
