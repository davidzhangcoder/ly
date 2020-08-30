package com.leyou.service;

public class DefaultTxMessage implements TxMessage {

    private String businessModule;
    private String businessKey;
    private String content;

    @Override
    public String businessModule() {
        return businessModule;
    }

    @Override
    public String businessKey() {
        return businessKey;
    }

    @Override
    public String content() {
        return content;
    }

    public void setBusinessModule(String businessModule) {
        this.businessModule = businessModule;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public void setContent(String content) {
        this.content = content;
    }
}