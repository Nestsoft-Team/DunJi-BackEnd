package com.dungzi.backend.global.common.error;

import com.dungzi.backend.global.common.Code;

public class ValidException extends DefaultException {
    public ValidException(Code code) {
        super(code);
    }
}
