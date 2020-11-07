package com.leyou.common.cache;

import java.util.Calendar;

public class OrderDtoCache {

    private long orderID;

    private long orderUniqueID;

    //1-Normal, 2-OnSale
    private int orderType;

    //1:排队中，2:秒杀等待支付,3:支付超时，4:秒杀失败,5:支付完成
    private int status;

    private Calendar createDate;

    private long onSaleProductID;

    private Long skuId; //商品skuId

    private Integer num; //购买数量

    public long getOnSaleProductID() {
        return onSaleProductID;
    }

    public void setOnSaleProductID(long onSaleProductID) {
        this.onSaleProductID = onSaleProductID;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public long getOrderUniqueID() {
        return orderUniqueID;
    }

    public void setOrderUniqueID(long orderUniqueID) {
        this.orderUniqueID = orderUniqueID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}