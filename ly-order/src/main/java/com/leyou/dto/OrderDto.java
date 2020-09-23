package com.leyou.dto;


import com.leyou.common.dto.CartDto;

import java.util.List;

public class OrderDto {

    private Long addressId; // 收获人地址id

    private Integer paymentType;// 付款类型

    private List<CartDto> carts;// 订单详情

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public List<CartDto> getCarts() {
        return carts;
    }

    public void setCarts(List<CartDto> carts) {
        this.carts = carts;
    }
}