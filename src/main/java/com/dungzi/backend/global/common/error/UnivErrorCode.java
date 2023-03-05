package com.dungzi.backend.global.common.error;

import com.dungzi.backend.global.common.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UnivErrorCode implements Code {

    UNIV_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 대학 코드입니다."), //404
    UNIV_DOMAIN_MISMATCH(HttpStatus.INTERNAL_SERVER_ERROR, "인증 대학과 이메일의 도메인이 일치하지 않습니다."), //500
    ;

    private HttpStatus code;
    private String  message;


    UnivErrorCode(HttpStatus code, String message) {
        this.code=code;
        this.message=message;
    }
}
