package com.leyou.seata;

//import com.leyou.dao.Account1Dao;
//import com.leyou.dao.Account2Dao;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestSeataServiceImpl implements TestSeataService {

//    @Autowired
//    private Account2Dao account2Dao;

    @Override
    @GlobalTransactional
    public void updateAccount2(long amount) {
//        account2Dao.account2AddAmount(2l, amount );
    }
}
