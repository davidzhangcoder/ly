package com.leyou.common.dto;

public class CartDto {

    private Long skuId; //商品skuId

    private Integer num; //购买数量

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