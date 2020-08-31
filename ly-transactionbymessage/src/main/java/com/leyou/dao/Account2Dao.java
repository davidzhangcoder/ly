package com.leyou.dao;

import com.leyou.domain.Account2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Account2Dao extends JpaRepository<Account2,Long>, JpaSpecificationExecutor<Account2> {

    @Modifying
    @Query(value="update t_account2 a set a.amount=a.amount + ?2 where a.id=?1" , nativeQuery=true)
    void account2AddAmount(Long id, Long amount);

}
