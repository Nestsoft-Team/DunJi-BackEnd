package com.dungzi.backend.domain.user.dto;

import lombok.Data;

import javax.validation.constraints.*;

public class UserRequestDto {

    @Data
    public static class UpdateEmailAuth {
        @Pattern(regexp = "^U[0-9]{4}$")
        @NotBlank
        private String univId;

        @Email
        @NotBlank
        private String univEmail;

        @AssertTrue
        @NotNull
        private Boolean isEmailChecked;
    }

    @Data
    public static class SignUpByKakao {
        @NotBlank
        private String kakaoAccessToken;

        @NotNull
        private Boolean isUnivAuth;

        @Pattern(regexp = "^U[0-9]{4}$")
        private String univId;

        @Email
        private String univEmail;

        @NotBlank
        private String nickname;
    }

    @Data
    public static class NicknameOnly {
        @NotBlank
        private String nickname;
    }
}
