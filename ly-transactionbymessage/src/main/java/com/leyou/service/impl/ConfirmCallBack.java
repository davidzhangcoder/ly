package com.leyou.service.impl;

import com.leyou.dao.TransactionalMessageDao;
import com.leyou.domain.TransactionalMessage;
import com.leyou.enums.TxMessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ConfirmCallBack implements RabbitTemplate.ConfirmCallback{

    @Autowired
    private TransactionalMessageManagementService transactionalMessageManagementService;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack) {
            // 标记成功
            transactionalMessageManagementService.doMarkSuccess( Long.parseLong(correlationData.getId()) );
        }
        else {
            transactionalMessageManagementService.doMarkFail( Long.parseLong(correlationData.getId()) , cause );
        }
    }


}
