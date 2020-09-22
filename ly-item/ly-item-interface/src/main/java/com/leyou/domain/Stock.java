package com.leyou.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="tb_stock")
public class Stock implements Serializable {

    @Id
    @Column(name = "sku_id", unique = true, nullable = false)
    private long skuId;

    @Column(name = "seckill_stock")
    private Long secKillStock;

    @Column(name = "seckill_total")
    private Long secKillTotal;

    @Column(name = "stock")
    private Long stock;

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    public Long getSecKillStock() {
        return secKillStock;
    }

    public void setSecKillStock(Long secKillStock) {
        this.secKillStock = secKillStock;
    }

    public Long getSecKillTotal() {
        return secKillTotal;
    }

    public void setSecKillTotal(Long secKillTotal) {
        this.secKillTotal = secKillTotal;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }
}
