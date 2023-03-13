package com.dungzi.backend.domain.user.application;

import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserUtilService {
    private final UserDao userDao;

    public void updateNickname(User user, String nickname) {
        log.info("[SERVICE] updateNickname");
        user.updateNickname(nickname);
        userDao.save(user);
    }



}
