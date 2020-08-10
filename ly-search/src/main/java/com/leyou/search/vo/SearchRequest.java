package com.leyou.search.vo;

import java.io.Serializable;

public class SearchRequest implements Serializable {

    private String key;

    private long page;

    private long size;

    private long categoryid3;

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
}
