package com.dungzi.backend.global.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    //bucketName
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private static S3Upload s3Upload;


    public String uploadProfileImage(UUID uuid, MultipartFile file) {
        log.info("[S3] Upload profile image");
        String contentType = "image/jpeg";

        String uploadPath = bucket + "/user/profile";

        String fileName = '/' + uuid.toString(); // 이전 프로필 사진은 덮어쓰기로 삭제됨

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            amazonS3.putObject(new PutObjectRequest(bucket, uploadPath+fileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (AmazonServiceException | IOException e) {
            log.error("[S3] Profile image Upload Error.");
            e.printStackTrace();
        }

        return amazonS3.getUrl(bucket, uploadPath+fileName).toString(); // 저장된 파일 url 찾아서 반환
    }


    public void uploadRoomFile(String RoomID, List<MultipartFile> file) {
        log.info("[S3] Upload File");
        String contentType = "image/jpeg";

        String uploadPath = bucket + "/room";

        for(int i = 0; i < file.size(); i++) {

            String fileName;
            if(i == 0) {
                fileName = '/' + RoomID + '/' + "main";
            }
            else {
                fileName = '/' + RoomID + '/' + i;
            }

            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(contentType);

                amazonS3.putObject(new PutObjectRequest(bucket, uploadPath+fileName, file.get(i).getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

            } catch (AmazonServiceException | IOException e) {
                log.error("[S3] Room File Upload Error.");
                e.printStackTrace();
            }
        }
    }

    public void uploadReviewFile(String ReviewID, List<MultipartFile> file) {
        log.info("[S3] Upload File");
        String contentType = "image/jpeg";

        String uploadPath = bucket + "/review";

        for(int i = 0; i < file.size(); i++) {

            String fileName;
            if(i == 0) {
                fileName = '/' + ReviewID + '/' + "main";
            }
            else {
                fileName = '/' + ReviewID + '/' + i;
            }

            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(contentType);

                amazonS3.putObject(new PutObjectRequest(bucket, uploadPath+fileName, file.get(i).getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

            } catch (AmazonServiceException | IOException e) {
                log.error("[S3] Review File Upload Error.");
                e.printStackTrace();
            }
        }
    }
}
