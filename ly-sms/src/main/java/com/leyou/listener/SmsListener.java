package com.leyou.listener;

import com.leyou.common.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class SmsListener {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SmsListener.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "sms.verify.code.queue",durable = "true"),
            exchange = @Exchange(name = "ly.sms.exchange",type = ExchangeTypes.TOPIC),
            key = {"sms.verify.code"}

    ))
    private void listenSms(Map<String,String> message) {
        if(!CollectionUtils.isEmpty( message )) {
            String code = message.get("code");
            String phone = message.get("phone");

            if( redisTemplate.opsForValue().get(Constants.KEY_PREFIX+phone) != null ) {
                logger.info("[短信服务] 发送短信频率过高，被拦截，手机号码：{}",phone);
                return;
            }

            redisTemplate.opsForValue().set(Constants.KEY_PREFIX+phone,code,3, TimeUnit.MINUTES);

            logger.info("[短信微服务] － 短信已发送 － 电话:{} , 验证码:{}" , phone , code);
        }
    }

}
