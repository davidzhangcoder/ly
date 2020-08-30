package com.leyou.dao;

import com.leyou.domain.TransactionalMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionalMessageDao extends JpaRepository<TransactionalMessage,Long>, JpaSpecificationExecutor<TransactionalMessage> {

//    void insertSelective(TransactionalMessage record);
//
//    void updateStatusSelective(TransactionalMessage record);

    @Query(value="SELECT * FROM t_transactional_message WHERE next_schedule_time >= :minScheduleTime AND next_schedule_time <= :maxScheduleTime " +
            "AND message_status <> :messageStatus AND current_retry_times < max_retry_times LIMIT :limit" ,nativeQuery=true)
    List<TransactionalMessage> queryPendingCompensationRecords(@Param("minScheduleTime") LocalDateTime minScheduleTime,
                                                               @Param("maxScheduleTime") LocalDateTime maxScheduleTime,
                                                               @Param("messageStatus") Integer messageStatus,
                                                               @Param("limit") int limit);
}
