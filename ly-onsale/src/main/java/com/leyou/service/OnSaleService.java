package com.leyou.service;

public interface OnSaleService {

    public long snapUpOrder(long onSaleProductID, long userID);

    public long snapUpOrderByUsingRedis(long onSaleProductID, long userID);

    public long snapUpOrderByUsingRabbitmq(long onSaleProductID, long userID);

    public int queryOnSaleStatus(long userID, long uniqueID);
}
