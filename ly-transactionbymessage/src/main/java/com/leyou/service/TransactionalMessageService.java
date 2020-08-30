package com.leyou.service;

public interface TransactionalMessageService {

    void sendTransactionalMessage(String bussinessKey , String Content);
}