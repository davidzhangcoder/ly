package com.leyou.common.exception;

import com.leyou.common.enums.ExceptionEnum;

public class LyException extends RuntimeException{

    private ExceptionEnum exceptionEnum;

    public LyException( ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }
}
