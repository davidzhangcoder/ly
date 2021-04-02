package com.leyou.configuration;

import com.leyou.service.impl.OnSaleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WaitingListConfirmCallBack implements RabbitTemplate.ConfirmCallback{

    private Logger logger = LoggerFactory.getLogger(WaitingListConfirmCallBack.class);

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //todo:
        if(ack) {
            // 标记成功
            logger.info("WaitingListConfirmCallBack - 成功");

//            transactionalMessageManagementService.doMarkSuccess( Long.parseLong(correlationData.getId()) );
        }
        else {
            logger.info("WaitingListConfirmCallBack - 失败");
//            transactionalMessageManagementService.doMarkFail( Long.parseLong(correlationData.getId()) , cause );
        }

    }
}
