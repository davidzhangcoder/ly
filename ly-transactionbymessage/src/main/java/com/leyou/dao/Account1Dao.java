package com.leyou.dao;

import com.leyou.domain.Account1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface Account1Dao extends JpaRepository<Account1,Long>, JpaSpecificationExecutor<Account1> {
}
