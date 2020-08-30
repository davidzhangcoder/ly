package com.leyou.dao;

import com.leyou.domain.TransactionalMessageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionalMessageContentDao extends JpaRepository<TransactionalMessageContent,Long>, JpaSpecificationExecutor<TransactionalMessageContent> {

//    void insert(TransactionalMessageContent record);

    public List<TransactionalMessageContent> findByMessageIdIn(List<Long> messageIds);

//    List<TransactionalMessageContent> queryByMessageIds(String messageIds);
}