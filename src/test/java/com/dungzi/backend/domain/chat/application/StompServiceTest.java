package com.dungzi.backend.domain.chat.application;

import static javax.swing.UIManager.put;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.config.security.jwt.JwtTokenProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class StompServiceTest {
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private StompService stompService;

    @Test
    @DisplayName("stomp 헤더에서 jwt토큰을 이용하여 유저 얻어오기")
    void testGetUserFromHeader() {
        //given
        StompHeaderAccessor accessor = mock(StompHeaderAccessor.class);
        String accessToken = "testAccessToken";
        String refreshToken = "testRefreshToken";
        String userNickName = "testUser";

        //stomp헤더 설정
        Map<String, String> attributes = new HashMap<>();
        attributes.put(JwtTokenProvider.ACCESS_TOKEN_HEADER_NAME, accessToken);
        attributes.put(JwtTokenProvider.REFRESH_TOKEN_HEADER_NAME, refreshToken);

        when(accessor.getHeader("simpSessionAttributes")).thenReturn(attributes);

        Authentication authentication = mock(Authentication.class);
        when(jwtTokenProvider.getAuthenticationByToken(accessToken)).thenReturn(authentication);

        User testUser = User.builder().userId(UUID.randomUUID()).nickname(userNickName).build();
        when(authentication.getPrincipal()).thenReturn(testUser);

        //when
        User result = stompService.getUserFromHeader(accessor);
        //then
        assertEquals(result.getNickname(), userNickName);
    }
}