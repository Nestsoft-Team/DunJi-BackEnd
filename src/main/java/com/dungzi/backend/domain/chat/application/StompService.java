package com.dungzi.backend.domain.chat.application;

import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.config.security.jwt.JwtTokenProvider;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StompService {

    private final JwtTokenProvider jwtTokenProvider;

    public User getUserFromHeader(StompHeaderAccessor accessor) {

        //TODO 지혜님한테 토큰으로 부터 어떻게 user객체 얻는지 확인하기
        Map<String, String> simpSessionAttributes = (Map<String, String>) accessor.getHeader("simpSessionAttributes");
        String accessToken = simpSessionAttributes.get(JwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
        String refreshToken = simpSessionAttributes.get(JwtTokenProvider.REFRESH_TOKEN_HEADER_NAME);

        Authentication authenticationByToken = jwtTokenProvider.getAuthenticationByToken(accessToken);

        return (User) authenticationByToken.getPrincipal();
    }
}
