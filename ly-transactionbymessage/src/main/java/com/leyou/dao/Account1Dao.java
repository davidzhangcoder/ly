package com.leyou.dao;

import com.leyou.domain.Account1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Account1Dao extends JpaRepository<Account1,Long>, JpaSpecificationExecutor<Account1> {

    @Modifying
    @Query(value="update t_account1 a set a.amount=a.amount + ?2 where a.id=?1" , nativeQuery=true)
    void account1AddAmount(Long id, Long amount);

}
