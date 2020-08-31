package com.leyou.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.dao.Account1Dao;
import com.leyou.dao.Account2Dao;
import com.leyou.domain.Account1;
import com.leyou.domain.Account2;
import com.leyou.service.BusinessServiceInterface;
import com.leyou.service.TransactionalMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Transactional
@Service( value = "BusinessService" )
public class BusinessService implements BusinessServiceInterface {

    @Autowired
    private Account1Dao account1Dao;

    @Autowired
    private Account2Dao account2Dao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionalMessageService transactionalMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    private Logger log = LoggerFactory.getLogger(TransactionalMessageManagementService.class);

    @Override
    @Transactional
    public void updateAmount1(int count) throws Exception {
//        Account1 account1 = account1Dao.getOne(1l );
//        account1.setAmount( account1.getAmount() -1 );
//        account1Dao.save(account1);

        account1Dao.account1AddAmount(1l, -1l);

        Map<String, Object> message = new HashMap<>();
        String toAccountId = "2";
        message.put("toAccountId", toAccountId);
        message.put("amount", "1");
        String content = objectMapper.writeValueAsString(message);

        transactionalMessageService.sendTransactionalMessage(
                toAccountId,
                content
        );

        //制造Exception
        log.info("Account 1: {} : {} ", count , Thread.currentThread().getName() );
    }

    @Override
    @Transactional
    public void updateAmount2(String id, String amount) {
//        Account2 account2 = account2Dao.getOne(Long.parseLong(id));
//        account2.setAmount( account2.getAmount() + Integer.parseInt(amount) );
//        account2Dao.save(account2);

        log.info("updateAmount2 begin");

        account2Dao.account2AddAmount(Long.parseLong(id),Long.parseLong(amount));

        log.info("Account 2:{}成功...", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrder() throws Exception {
        String orderId = UUID.randomUUID().toString();
        BigDecimal amount = BigDecimal.valueOf(100L);
        jdbcTemplate.update("INSERT INTO t_order(order_id,amount) VALUES (?,?)", p -> {
            p.setString(1, orderId);
            p.setBigDecimal(2, amount);
        });
        Map<String, Object> message = new HashMap<>();
        message.put("orderId", orderId);
        message.put("amount", amount);
        String content = objectMapper.writeValueAsString(message);

        transactionalMessageService.sendTransactionalMessage(
                orderId,
                content
        );

        //制造Exception
        //int a = 1/0;

        log.info("保存订单:{}成功...", orderId);
    }
}