package com.leyou.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="tb_spu")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Spu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne(targetEntity=Brand.class/*,fetch=FetchType.LAZY*/)
    @JoinColumn(name="brand_id",referencedColumnName="id")
    private Brand brandId;

    /**
     * 1级类目
     */
    @OneToOne(targetEntity=Category.class/*,fetch=FetchType.LAZY*/)
    @JoinColumn(name="cid1",referencedColumnName="id")
    private Category category1;

    /**
     * 2级类目
     */
    @OneToOne(targetEntity=Category.class/*,fetch=FetchType.LAZY*/)
    @JoinColumn(name="cid2",referencedColumnName="id")
    private Category category2;

    /**
     * 3级类目
     */
    @OneToOne(targetEntity=Category.class/*,fetch=FetchType.LAZY*/)
    @JoinColumn(name="cid3",referencedColumnName="id")
    private Category category3;

    /**
     * 标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 子标题
     */
    @Column(name = "sub_title")
    private String subTitle;

    /**
     * 是否上架
     */
    @Column(name = "saleable")
    private Boolean saleable;

    /**
     * 是否有效，逻辑删除使用
     */
    @Column(name = "valid")
    private Boolean valid;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Brand getBrandId() {
        return brandId;
    }

    public void setBrandId(Brand brandId) {
        this.brandId = brandId;
    }

    public Category getCategory1() {
        return category1;
    }

    public void setCategory1(Category category1) {
        this.category1 = category1;
    }

    public Category getCategory2() {
        return category2;
    }

    public void setCategory2(Category category2) {
        this.category2 = category2;
    }

    public Category getCategory3() {
        return category3;
    }

    public void setCategory3(Category category3) {
        this.category3 = category3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Boolean getSaleable() {
        return saleable;
    }

    public void setSaleable(Boolean saleable) {
        this.saleable = saleable;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
