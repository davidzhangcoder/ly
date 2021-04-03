package com.leyou.configuration;

import com.leyou.service.impl.OnSaleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WaitingListConfirmCallBack implements RabbitTemplate.ConfirmCallback{

//    https://blog.csdn.net/qq_38846242/article/details/84256383
//    实现ConfirmCallback并重写confirm(CorrelationData correlationData, boolean ack, String cause)回调方法，可以确认消息是否发送到Exchange

    private Logger logger = LoggerFactory.getLogger(WaitingListConfirmCallBack.class);

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //todo:
        if(ack) {
            // 标记成功
            System.out.println("WaitingListConfirmCallBack - 到达exchange - ack:成功" + " correlationData:id(OnSaleStatus's uniqueID)="+correlationData.getId() );

//            transactionalMessageManagementService.doMarkSuccess( Long.parseLong(correlationData.getId()) );
        }
        else {
            System.out.println("WaitingListConfirmCallBack - 到达exchange - ack:失败" + " correlationData:id(OnSaleStatus's uniqueID)="+correlationData.getId() );
//            transactionalMessageManagementService.doMarkFail( Long.parseLong(correlationData.getId()) , cause );
        }

    }
}
