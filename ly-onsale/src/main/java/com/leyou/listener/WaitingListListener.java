package com.leyou.listener;

import com.leyou.common.dto.OnSaleStatus;
import com.leyou.service.impl.OnSaleAsyncCreater;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
@RabbitListener(queues = "leyou.waitinglist.queue")
public class WaitingListListener {

    @Autowired
    @Qualifier("onSaleThreadPool_OnSaleService")
    public Executor onSaleThreadPool;

    @Autowired
    private OnSaleAsyncCreater onSaleAsyncCreater;

    private void listenTransactionMessage(OnSaleStatus onSaleStatus){
        String hashTag = "_{OnSaleServiceImpl_snapUpOrder}";

        for (int i = 0; i < 3 ; i++) {
            Thread thread = new Thread(() -> {
                while( true ) {
                    if (onSaleStatus != null) {
                        long userID = onSaleStatus.getUserID();
                        Long onSaleProductID = onSaleStatus.getGoodsId();

                        onSaleAsyncCreater.snapUpOrder(onSaleProductID, userID, hashTag);
                    }
                }
            });

            onSaleThreadPool.execute( thread );
        }
    }

}
