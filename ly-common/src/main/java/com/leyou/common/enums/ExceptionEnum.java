package com.leyou.common.enums;

import org.springframework.http.HttpStatus;

public enum ExceptionEnum {

    NAME_IS_NULL(HttpStatus.BAD_REQUEST.value(),"姓名不能为空"),
    BRAND_SEARCH_LIST_IS_EMPTY(HttpStatus.NOT_FOUND.value(),"品牌搜索结果为空"),

    DATA_CHECK_TYPE_NOT_EXIST(HttpStatus.BAD_REQUEST.value(),"数据校验类型错误"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST.value(),"参数错误"),

    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(),"用户已存在"),
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST.value(),"用户不存在"),
    USER_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST.value(),"密码错误"),
    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(),"未授权用户"),

    CODE_EXPIRED(HttpStatus.BAD_REQUEST.value(),"注册码已过期"),
    CODE_INVALID(HttpStatus.BAD_REQUEST.value(),"注册码错误"),

    AUTH_TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(),"令牌生成错误"),
    AUTH_PUBLIC_KEY_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR.value(),"公钥文件不存在1"),

    CREATE_ORDER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(),"创建订单错误"),
    STOCK_NOT_ENOUGH(HttpStatus.INTERNAL_SERVER_ERROR.value(),"扣减库存错误"),
    STOCK_RETRIEVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(),"读取库存错误"),

    ORDER_CREATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(),"订单生成错误")
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
