package com.leyou.common.enums;

public enum ExchangeType {

    FANOUT("fanout"),

    DIRECT("direct"),

    TOPIC("topic"),

    DEFAULT(""),

    ;

    private final String type;

    ExchangeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}