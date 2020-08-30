package com.leyou.service;

// 事务消息
public interface TxMessage {

    String businessModule();

    String businessKey();

    String content();
}