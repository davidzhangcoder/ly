package com.leyou.common.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OnSaleStatus {

    //秒杀用户名
    private long userID;

    //创建时间
    private Calendar createTime;

    //秒杀状态  1:排队中，2:秒杀等待支付,3:支付超时，4:秒杀失败,5:支付完成
    private Integer status;

    //秒杀的商品ID
    private Long goodsId;

    //应付金额
    private Float money;

    //订单号
    private Long orderId;

    //时间段
    private String time;

    private long uniqueID;

    private String reason;

    //for test redis, useless here
    private List<CartDto> cartDtoList = new ArrayList<CartDto>();

    public OnSaleStatus() {
    }

    public OnSaleStatus(long userID, Calendar createTime, Integer status, Long goodsId, String time, long uniqueID) {
        this.userID = userID;
        this.createTime = createTime;
        this.status = status;
        this.goodsId = goodsId;
        this.time = time;
        this.uniqueID = uniqueID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(long uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<CartDto> getCartDtoList() {
        return cartDtoList;
    }

    public void setCartDtoList(List<CartDto> cartDtoList) {
        this.cartDtoList = cartDtoList;
    }

    @Override
    public String toString() {
        return "OnSaleStatus{" +
                "userID=" + userID +
                ", createTime=" + createTime +
                ", status=" + status +
                ", goodsId=" + goodsId +
                ", money=" + money +
                ", orderId=" + orderId +
                ", time='" + time + '\'' +
                ", uniqueID=" + uniqueID +
                ", reason='" + reason + '\'' +
                '}';
    }
}
