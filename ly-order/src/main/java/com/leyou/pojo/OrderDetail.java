package com.leyou.pojo;

import javax.persistence.*;

/**
 * @Feature: 订单详情信息实体类
 */
@Entity
@Table(name = "tb_order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 商品id
     */
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 商品购买数量
     */
    @Column(name = "num")
    private Integer num;

    /**
     * 商品标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 商品单价
     */
    @Column(name = "price")
    private Long price;

    /**
     * 商品规格数据
     */
    @Column(name = "own_spec")
    private String ownSpec;

    /**
     * 图片
     */
    @Column(name = "image")
    private String image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getOwnSpec() {
        return ownSpec;
    }

    public void setOwnSpec(String ownSpec) {
        this.ownSpec = ownSpec;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", skuId=" + skuId +
                ", num=" + num +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", ownSpec='" + ownSpec + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}