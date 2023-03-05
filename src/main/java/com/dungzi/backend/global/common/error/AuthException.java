package com.dungzi.backend.global.common.error;

import com.dungzi.backend.global.common.Code;


public class AuthException extends DefaultException {
    public AuthException(Code code) {
        super(code);
    }
}
