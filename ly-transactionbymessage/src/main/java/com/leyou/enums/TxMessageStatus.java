package com.leyou.enums;

// 消息状态

public enum TxMessageStatus {

    /**
     * 成功
     */
    SUCCESS(1),

    /**
     * 待处理
     */
    PENDING(0),

    /**
     * 处理失败
     */
    FAIL(-1),

    ;

    private final Integer status;

    TxMessageStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}