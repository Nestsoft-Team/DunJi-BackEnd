package com.dungzi.backend.global.common.error;

import com.dungzi.backend.global.common.Code;

public class UnivException extends DefaultException {
    public UnivException(Code code) {
        super(code);
    }
}
