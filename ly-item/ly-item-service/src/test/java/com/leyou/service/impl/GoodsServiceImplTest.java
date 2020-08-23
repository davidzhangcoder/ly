package com.leyou.service.impl;

import com.leyou.domain.Spu;
import com.leyou.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceImplTest {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private GoodsService goodsService;

    @Test
    public void testSendMessage() {
        Spu spu = goodsService.testPersistSpu(57, " Test1 ");

        amqpTemplate.convertAndSend("item.update", spu.getId() );
    }

}