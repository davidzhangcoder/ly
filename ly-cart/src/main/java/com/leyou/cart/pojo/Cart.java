package com.leyou.cart.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="购物车信息")
public class Cart {

    @ApiModelProperty(value="SKU id")
    private Long skuId;

    @ApiModelProperty(value="名称")
    private String title;

    @ApiModelProperty(value="图片")
    private String image;

    @ApiModelProperty(value="价格")
    private Long price;

    @ApiModelProperty(value="数量")
    private Integer num;

    @ApiModelProperty(value="私有参数")
    private String ownSpec;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getOwnSpec() {
        return ownSpec;
    }

    public void setOwnSpec(String ownSpec) {
        this.ownSpec = ownSpec;
    }
}
