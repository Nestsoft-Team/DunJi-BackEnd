package com.dungzi.backend.domain.user.application;

import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.response.code.AuthErrorCode;
import com.dungzi.backend.global.common.response.exception.AuthException;
import com.dungzi.backend.global.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserUtilService {
    private final UserDao userDao;
    private final FileUploadService fileUploadService;

    public void updateNickname(User user, String nickname) {
        log.info("[SERVICE] updateNickname");
        user.updateNickname(nickname);
        userDao.save(user);
    }

    public void updateProfileImage(User user, MultipartFile profileImg) {
        log.info("[SERVICE] updateProfileImage");
        UUID uuid = user.getUserId();
        String imageUrl = fileUploadService.uploadProfileImage(uuid, profileImg);
        user.updateProfileImg(imageUrl);
        userDao.save(user);
    }

}
