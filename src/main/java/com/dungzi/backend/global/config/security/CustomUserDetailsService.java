package com.dungzi.backend.global.config.security;

import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.global.common.error.AuthException;
import com.dungzi.backend.global.common.error.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    //토큰에서 유저 정보 확인한 후 정보로 User 객체를 가져올 때 쓰임
    @Override
    public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {
        return userDao.findByUserId(UUID.fromString(uuid)) //User implements UserDetails
                .orElseThrow(() -> new AuthException(AuthErrorCode.NOT_EXIST_USER)); //TODO : 이거 왜 Handler에 안잡힐까..
    }
}