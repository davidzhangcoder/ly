package com.leyou.dao;

import com.leyou.domain.Account2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface Account2Dao extends JpaRepository<Account2,Long>, JpaSpecificationExecutor<Account2> {
}
