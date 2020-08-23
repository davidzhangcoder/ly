package com.leyou.common.vo;

import java.util.Calendar;

public class ExceptionResult {

    private int code;
    private String message;
    private Calendar createdTime;

    public ExceptionResult(int code, String message) {
        this.code = code;
        this.message = message;
        createdTime = Calendar.getInstance();
    }

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

    public Calendar getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Calendar createdTime) {
        this.createdTime = createdTime;
    }
}
