package com.leyou.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="t_transactional_message")
public class TransactionalMessage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "edit_time")
    private LocalDateTime editTime;

    @Column(name = "creator")
    private String creator;

    @Column(name = "editor")
    private String editor;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "current_retry_times")
    private Integer currentRetryTimes;

    @Column(name = "max_retry_times")
    private Integer maxRetryTimes;

    @Column(name = "queue_name")
    private String queueName;

    @Column(name = "exchange_name")
    private String exchangeName;

    @Column(name = "exchange_type")
    private String exchangeType;

    @Column(name = "routing_key")
    private String routingKey;

    @Column(name = "business_module")
    private String businessModule;

    @Column(name = "business_key")
    private String businessKey;

    @Column(name = "next_schedule_time")
    private LocalDateTime nextScheduleTime;

    @Column(name = "message_status")
    private Integer messageStatus;

    @Column(name = "init_backoff")
    private Long initBackoff;

    @Column(name = "backoff_factor")
    private Integer backoffFactor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getEditTime() {
        return editTime;
    }

    public void setEditTime(LocalDateTime editTime) {
        this.editTime = editTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getCurrentRetryTimes() {
        return currentRetryTimes;
    }

    public void setCurrentRetryTimes(Integer currentRetryTimes) {
        this.currentRetryTimes = currentRetryTimes;
    }

    public Integer getMaxRetryTimes() {
        return maxRetryTimes;
    }

    public void setMaxRetryTimes(Integer maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getBusinessModule() {
        return businessModule;
    }

    public void setBusinessModule(String businessModule) {
        this.businessModule = businessModule;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public LocalDateTime getNextScheduleTime() {
        return nextScheduleTime;
    }

    public void setNextScheduleTime(LocalDateTime nextScheduleTime) {
        this.nextScheduleTime = nextScheduleTime;
    }

    public Integer getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Integer messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Long getInitBackoff() {
        return initBackoff;
    }

    public void setInitBackoff(Long initBackoff) {
        this.initBackoff = initBackoff;
    }

    public Integer getBackoffFactor() {
        return backoffFactor;
    }

    public void setBackoffFactor(Integer backoffFactor) {
        this.backoffFactor = backoffFactor;
    }
}
