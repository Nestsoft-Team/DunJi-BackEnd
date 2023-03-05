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

    private static final String STOMP_HEADER_ATTR = "simpSessionAttributes";
    private final JwtTokenProvider jwtTokenProvider;

    public User getUserFromHeader(StompHeaderAccessor accessor) {
        Map<String, String> simpSessionAttributes = (Map<String, String>) accessor.getHeader(STOMP_HEADER_ATTR);
        String accessToken = simpSessionAttributes.get(JwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
        String refreshToken = simpSessionAttributes.get(JwtTokenProvider.REFRESH_TOKEN_HEADER_NAME);

        Authentication authenticationByToken = jwtTokenProvider.getAuthenticationByToken(accessToken);

        return (User) authenticationByToken.getPrincipal();
    }
}
