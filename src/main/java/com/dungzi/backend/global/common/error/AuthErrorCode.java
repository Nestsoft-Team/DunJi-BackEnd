package com.dungzi.backend.global.common.error;

import com.dungzi.backend.global.common.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode implements Code {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."), //401
    GUEST_USER(HttpStatus.UNAUTHORIZED, "사용자 정보를 확인할 수 없습니다. 비회원 사용자입니다."), //401
    NOT_EXIST_USER(HttpStatus.UNAUTHORIZED, "이 uuid 에 해당하는 사용자가 존재하지 않습니다."), //401
    ALREADY_EXIST_USER(HttpStatus.CONFLICT, "이 email 에 해당하는 사용자가 이미 존재합니다."), //409
    KAKAO_FAILED(HttpStatus.CONFLICT, "카카오 관련 처리를 실행할 수 없습니다."), //500
    ;

    private HttpStatus code;
    private String  message;


    AuthErrorCode(HttpStatus code, String message) {
        this.code=code;
        this.message=message;
    }
}
