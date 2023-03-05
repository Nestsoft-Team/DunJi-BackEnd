package com.dungzi.backend.global.s3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Upload {

    //bucketName
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    private final String formatName = "jpg";	// 파일 포맷 jpg로 통일


    // 파일 업로드
    public void fileUpload(String fileName, byte[] fileData) throws FileNotFoundException {

        try {
            String filePath = (fileName).replace(File.separatorChar, '/');
            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentLength(fileData.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData);

            amazonS3Client.putObject(bucket, filePath, byteArrayInputStream, metaData);
            log.info("[S3 Upload] File Upload Success");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 파일 삭제
    public void fileDelete(String fileName) {

        try {
            String imgName = (fileName).replace(File.separatorChar, '/');
            amazonS3Client.deleteObject(bucket, imgName);
//            log.info("File Delete Success");

        } catch (Exception e) {
//            log.error("File Delete Error");
            e.printStackTrace();
        }

    }
}