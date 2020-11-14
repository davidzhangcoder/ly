package com.leyou.service;

public interface OnSaleService {

    public long snapUpOrder(long onSaleProductID, long userID);

    public void queryOnSaleStatus(long uniqueID);
}
