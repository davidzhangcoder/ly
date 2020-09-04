package com.leyou.seata;

import com.leyou.client.UserClient;
import com.leyou.dao.Account1Dao;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class TestSeataServiceImpl implements TestSeataService {

    @Autowired
    private Account1Dao account1Dao;

    @Autowired
    private UserClient userClient;

    @Override
    @GlobalTransactional
    public void updateAccount1() {
        account1Dao.account1AddAmount(1l, -1l);

        String seataUpdateAccount2 = userClient.seataUpdateAccount2(1);

        System.out.println(seataUpdateAccount2);

        int a = 1/0;
    }
}
