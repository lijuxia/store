package org.ljx.entity.web;

/**
 * 分页条件
 * Created by ljx on 2018/5/15.
 */
public class PageSearch {
    private int pageSize = 10;
    private int pageNum = 1;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
