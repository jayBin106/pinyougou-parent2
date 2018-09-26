package com.pinyougou.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/9/26.
 */
public class PageResult implements Serializable {
    //总计路数
    private long total;

    //当前页结果
    private List row;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRow() {
        return row;
    }

    public void setRow(List row) {
        this.row = row;
    }

    public PageResult() {
    }

    public PageResult(long total, List row) {
        this.total = total;
        this.row = row;
    }
}
