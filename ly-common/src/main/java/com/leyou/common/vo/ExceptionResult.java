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
}
