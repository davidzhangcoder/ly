package com.leyou.search.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SearchRequest implements Serializable {

    private String key;

    private long page;

    private long size;

    private long categoryid3;

    private Map<String,String> specfilters = new HashMap<String,String>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getPage() {
        return page - 1;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCategoryid3() {
        return categoryid3;
    }

    public void setCategoryid3(long categoryid3) {
        this.categoryid3 = categoryid3;
    }

    public Map<String, String> getSpecfilters() {
        return specfilters;
    }

    public void setSpecfilters(Map<String, String> specfilters) {
        this.specfilters = specfilters;
    }
}
