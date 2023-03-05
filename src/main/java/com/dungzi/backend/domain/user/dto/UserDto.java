package com.dungzi.backend.domain.user.dto;

import com.dungzi.backend.domain.user.domain.User;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class UserDto {

    private String userId;
    private String ci;
    //    private String token;
//    private String userName;
    private String nickname;
    private String phoneNum;
    private String userType;
    private String gender;
    private Boolean authCheck;
    private String email;
    private String profileImg;
    private String univName;
    private ZonedDateTime regDate;
    private Date delDate;

    public User toEntity() {
        UUID uuid = null;
        if(userId != null){
            uuid = UUID.fromString(userId);
        }

        return User.builder()
                .userId(uuid)
                .ci(ci)
//                .token(token)
//                .userName(userName)
                .nickname(nickname)
                .phoneNum(phoneNum)
//                .userType(userType)
                .gender(gender)
//                .authCheck(authCheck)
                .email(email)
                .profileImg(profileImg)
//                .univName(univName)
//                .regDate(regDate)
                .delDate(delDate)
                .build();

    }
}