package com.dungzi.backend.domain.user.dto;

import com.dungzi.backend.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class UserAuthResponseDto {

    @Builder
    @Data
    public static class SendEmailAuth {
        private String email;
        private String authCode;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class KakaoLogin {
        private Boolean isUser;
        private UUID uuid;
        private String kakaoAccessToken;
    }

    @Builder
    @Data
    public static class SignUpByKakao {
        private UUID uuid;

        public static SignUpByKakao toDto(User user){
            return SignUpByKakao.builder()
                    .uuid(user.getUserId())
                    .build();
        }
    }

    @Builder
    @Data
    public static class CheckNicknameExist {
        private String nickname;
        private Boolean isUnique;

        public static CheckNicknameExist toDto(String nickname, Boolean isUnique){
            return CheckNicknameExist.builder()
                    .nickname(nickname)
                    .isUnique(isUnique)
                    .build();
        }
    }
}
