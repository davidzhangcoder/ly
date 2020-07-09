package com.leyou.common.enums;

import org.springframework.http.HttpStatus;

public enum ExceptionEnum {

    NAME_IS_NULL(HttpStatus.BAD_REQUEST.value(),"姓名不能为空")
    ;

    private ExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
