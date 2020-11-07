package com.leyou.dto;


import com.leyou.common.dto.CartDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value="订单信息")
public class OrderDto {

    @ApiModelProperty(value="收获人地址id")
    private Long addressId; // 收获人地址id

    @ApiModelProperty(value="付款类型")
    private Integer paymentType;// 付款类型

    @ApiModelProperty(value="订单详情")
    private List<CartDto> carts = new ArrayList<CartDto>();// 订单详情

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