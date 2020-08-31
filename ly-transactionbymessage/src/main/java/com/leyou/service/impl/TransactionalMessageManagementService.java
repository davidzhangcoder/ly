package com.leyou.service.impl;

import com.leyou.dao.TransactionalMessageContentDao;
import com.leyou.dao.TransactionalMessageDao;
import com.leyou.domain.TransactionalMessage;
import com.leyou.domain.TransactionalMessageContent;

import com.leyou.enums.TxMessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionalMessageManagementService {

    @Autowired
    private TransactionalMessageDao messageDao;

    @Autowired
    private TransactionalMessageContentDao contentDao;

    @Autowired
    @Qualifier(value="leyouTransactionRabbitTemplate")
    private RabbitTemplate leyouTransactionRabbitTemplate;

    private Logger log = LoggerFactory.getLogger(TransactionalMessageManagementService.class);

    private static final LocalDateTime END = LocalDateTime.of(2999, 1, 1, 0, 0, 0);
    private static final long DEFAULT_INIT_BACKOFF = 10L;
    private static final int DEFAULT_BACKOFF_FACTOR = 2;
    private static final int DEFAULT_MAX_RETRY_TIMES = 5;
    private static final int LIMIT = 100;

    @Transactional
    public TransactionalMessage doMarkSuccess(long id){
        TransactionalMessage transactionalMessage = messageDao.getOne(id);
        markSuccess(transactionalMessage);
        return transactionalMessage;
    }

    @Transactional
    public TransactionalMessage doMarkFail(long id, String cause){
        TransactionalMessage transactionalMessage = messageDao.getOne(id);
        markFail(transactionalMessage,cause);
        return transactionalMessage;
    }



    public void saveTransactionalMessageRecord(TransactionalMessage record, String content) {
        record.setMessageStatus(TxMessageStatus.PENDING.getStatus());
        record.setNextScheduleTime(calculateNextScheduleTime(LocalDateTime.now(), DEFAULT_INIT_BACKOFF,
                DEFAULT_BACKOFF_FACTOR, 0));
        record.setCurrentRetryTimes(0);
        record.setInitBackoff(DEFAULT_INIT_BACKOFF);
        record.setBackoffFactor(DEFAULT_BACKOFF_FACTOR);
        record.setMaxRetryTimes(DEFAULT_MAX_RETRY_TIMES);
        messageDao.save(record);
        TransactionalMessageContent messageContent = new TransactionalMessageContent();
        messageContent.setContent(content);
        messageContent.setMessageId(record.getId());
        contentDao.save(messageContent);
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendMessageSync(final TransactionalMessage record, String content) {

        CorrelationData correlationData = new CorrelationData( record.getId() + "" );

        leyouTransactionRabbitTemplate.convertAndSend(record.getExchangeName(), record.getRoutingKey(), content , correlationData );

        log.info("sendMessageSync : " + Thread.currentThread().getName());

    }

    private void markSuccess(TransactionalMessage record) {
        log.info("发送消息成功 :{}", Thread.currentThread().getName());
        // 标记下一次执行时间为最大值
        record.setNextScheduleTime(END);
        record.setCurrentRetryTimes(record.getCurrentRetryTimes().compareTo(record.getMaxRetryTimes()) >= 0 ?
                record.getMaxRetryTimes() : record.getCurrentRetryTimes() + 1);
        record.setMessageStatus(TxMessageStatus.SUCCESS.getStatus());
        record.setEditTime(LocalDateTime.now());
        messageDao.save(record);
    }

    private void markFail(TransactionalMessage record, String e) {
        log.info("发送消息失败 :{}", Thread.currentThread().getName());
        record.setCurrentRetryTimes(record.getCurrentRetryTimes().compareTo(record.getMaxRetryTimes()) >= 0 ?
                record.getMaxRetryTimes() : record.getCurrentRetryTimes() + 1);
        // 计算下一次的执行时间
        LocalDateTime nextScheduleTime = calculateNextScheduleTime(
                record.getNextScheduleTime(),
                record.getInitBackoff(),
                record.getBackoffFactor(),
                record.getCurrentRetryTimes()
        );
        record.setNextScheduleTime(nextScheduleTime);
        record.setMessageStatus(TxMessageStatus.FAIL.getStatus());
        record.setEditTime(LocalDateTime.now());
        messageDao.save(record);
    }

    /**
     * 计算下一次执行时间
     *
     * @param base          基础时间
     * @param initBackoff   退避基准值
     * @param backoffFactor 退避指数
     * @param round         轮数
     * @return LocalDateTime
     */
    public LocalDateTime calculateNextScheduleTime(LocalDateTime base,
                                                    long initBackoff,
                                                    long backoffFactor,
                                                    long round) {
        double delta = initBackoff * Math.pow(backoffFactor, round);
        return base.plusSeconds((long) delta);
    }

    /**
     * 推送补偿 - 里面的参数应该根据实际场景定制
     */
    public void processPendingCompensationRecords() {
        // 时间的右值为当前时间减去退避初始值，这里预防把刚保存的消息也推送了
        LocalDateTime max = LocalDateTime.now().plusSeconds(-DEFAULT_INIT_BACKOFF);
        // 时间的左值为右值减去1小时
        LocalDateTime min = max.plusHours(-1);
        Map<Long, TransactionalMessage> collect = messageDao.queryPendingCompensationRecords(min, max, TxMessageStatus.SUCCESS.getStatus() , LIMIT)
                .stream()
                .collect(Collectors.toMap(TransactionalMessage::getId, x -> x));
        if (!collect.isEmpty()) {
            List<Long> messageIds = new ArrayList<Long>();
            messageIds.addAll(collect.keySet());
            contentDao.findByMessageIdIn(messageIds).forEach(item -> {
                TransactionalMessage message = collect.get(item.getMessageId());
                sendMessageSync(message, item.getContent());
            });


//            StringJoiner joiner = new StringJoiner(",", "(", ")");
//            collect.keySet().forEach(x -> joiner.add(x.toString()));

//            contentDao.queryByMessageIds(joiner.toString())
//                    .forEach(item -> {
//                        TransactionalMessage message = collect.get(item.getMessageId());
//                        sendMessageSync(message, item.getContent());
//                    });
        }
    }
}