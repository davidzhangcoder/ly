package com.leyou.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="tb_spec_param")
public class SpecParam implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne(targetEntity=Category.class,fetch=FetchType.LAZY)
    @JoinColumn(name="cid",referencedColumnName="id")
    private Category category;

    @Column(name = "cid", insertable = false , updatable = false)
    private long cid;

    @Column(name = "group_id")
    private long specGroupID;

    @Column(name = "name")
    //@JsonProperty( value = "k" )
    private String name;

    //numeric在sql里是一个关键字，加上``,变成字符
    @Column(name = "`numeric`")
    @JsonProperty( value = "numerical" )
    private Boolean isNumeric;

    @Column(name = "unit")
    private String unit;

    @Column(name = "generic")
    @JsonProperty( value = "global" )
    private Boolean generic;

    @Column(name = "searching")
    @JsonProperty( value = "searchable" )
    private Boolean searching;

    @Column(name = "segments")
    private String segments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public long getSpecGroupID() {
        return specGroupID;
    }

    public void setSpecGroupID(long specGroupID) {
        this.specGroupID = specGroupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNumeric() {
        return isNumeric;
    }

    public void setNumeric(Boolean numeric) {
        isNumeric = numeric;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getGeneric() {
        return generic;
    }

    public void setGeneric(Boolean generic) {
        this.generic = generic;
    }

    public Boolean getSearching() {
        return searching;
    }

    public void setSearching(Boolean searching) {
        this.searching = searching;
    }

    public String getSegments() {
        return segments;
    }

    public void setSegments(String segments) {
        this.segments = segments;
    }
}
