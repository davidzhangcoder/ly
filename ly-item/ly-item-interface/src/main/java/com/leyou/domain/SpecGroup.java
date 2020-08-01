package com.leyou.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="tb_spec_group")
public class SpecGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @OneToOne(targetEntity=Category.class,fetch=FetchType.LAZY)
    @JoinColumn(name="cid",referencedColumnName="id")
    private Category category;

    @Column(name = "name")
    @JsonProperty( value = "group")
    private String name;

    @OneToMany(targetEntity=SpecParam.class,fetch=FetchType.LAZY,mappedBy="specGroupID")
//    @JsonProperty( value = "params")
    private List<SpecParam> specParamList = new ArrayList<SpecParam>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpecParam> getSpecParamList() {
        return specParamList;
    }

    public void setSpecParamList(List<SpecParam> specParamList) {
        this.specParamList = specParamList;
    }
}
