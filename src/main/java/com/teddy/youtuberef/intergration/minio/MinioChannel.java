package com.teddy.youtuberef.intergration.minio;

import com.teddy.youtuberef.web.rest.error.BusinessException;
import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioChannel {
    private final MinioClient  minioClient;
    private static final String BUCKET = "resource";

    @PostConstruct
    private void init(){
        createBucket("resource");
    }

    @SneakyThrows
    private void createBucket(final String bucketName) {
        // Kiểm tra nếu bucket đã tồn tại
        final var found = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
        );

        if(!found){
            // Taọ bucket nếu chua tồn tại
            minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
            );

            // Thiết lập bucket là public bằng cách set policy
            final var policy = """
                        {
                          "Version": "2012-10-17",
                          "Statement": [
                           {
                              "Effect": "Allow",
                              "Principal": "*",
                              "Action": "s3:GetObject",
                              "Resource": "arn:aws:s3:::%s/*"
                            }
                          ]
                        }
                    """.formatted(bucketName);
            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build()
            );
        }else
            log.info("Bucket %s đã tồn tại.".formatted(bucketName));
    }

    public String upload(@NonNull final MultipartFile avatar) {
        log.info("Bucket: {}, file size: {}", BUCKET, avatar.getSize());
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET)
                            .object(avatar.getOriginalFilename())
                            .contentType(Objects.isNull(avatar.getContentType()) ? "image/png; image/jpg" : avatar.getContentType())
                            .stream((avatar.getInputStream()), avatar.getSize(), -1)
                            .build()
            );
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(BUCKET)
                            .object(avatar.getOriginalFilename())
                            .build()
            );
        } catch (Exception e) {
            log.error("Error saving image \n {} ", e.getMessage());
            throw new BusinessException("400","Unable to upload image", e);
        }


    }
}
