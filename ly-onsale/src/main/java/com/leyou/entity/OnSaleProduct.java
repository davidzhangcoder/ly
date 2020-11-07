package com.leyou.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//@Entity
//@Table(name = "tb_seckill_goods")
@ApiModel(value="秒杀商品")
public class OnSaleProduct implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @ApiModelProperty(value="秒杀商品id")
    private Long id;//

    @Column(name = "sup_id")
    @ApiModelProperty(value="spu ID")
    private Long supId;//spu ID

    @Column(name = "sku_id")
    @ApiModelProperty(value="sku ID")
    private Long skuId;//sku ID

    @Column(name = "name")
    @ApiModelProperty(value="标题")
    private String name;//标题

    @Column(name = "small_pic")
    @ApiModelProperty(value="商品图片")
    private String smallPic;//商品图片

    @Column(name = "price")
    @ApiModelProperty(value="原价格")
    private String price;//原价格

    @Column(name = "cost_price")
    @ApiModelProperty(value="秒杀价格")
    private String costPrice;//秒杀价格

    @Column(name = "create_time")
    @ApiModelProperty(value="添加日期")
    private Date createTime;//添加日期

    @Column(name = "check_time")
    @ApiModelProperty(value="审核日期")
    private Date checkTime;//审核日期

    @Column(name = "status")
    @ApiModelProperty(value="审核状态，0未审核，1审核通过，2审核不通过")
    private String status;//审核状态，0未审核，1审核通过，2审核不通过

    @Column(name = "start_time")
    @ApiModelProperty(value="开始时间")
    private Date startTime;//开始时间

    @Column(name = "end_time")
    @ApiModelProperty(value="结束时间")
    private Date endTime;//结束时间

    @Column(name = "num")
    @ApiModelProperty(value="秒杀商品数")
    private Integer num;//秒杀商品数

    @Column(name = "stock_count")
    @ApiModelProperty(value="剩余库存数")
    private Integer stockCount;//剩余库存数

    @Column(name = "introduction")
    @ApiModelProperty(value="描述")
    private String introduction;//描述

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupId() {
        return supId;
    }

    public void setSupId(Long supId) {
        this.supId = supId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
