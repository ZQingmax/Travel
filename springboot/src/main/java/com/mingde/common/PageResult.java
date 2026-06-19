package com.mingde.common;

import java.util.Collections;
import java.util.List;

public class PageResult<T> {

    private List<T> list = Collections.emptyList();
    private long total;
    private long pageNum;
    private long pageSize;
    private long pages;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list == null ? Collections.emptyList() : list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }
}