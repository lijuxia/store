package org.ljx.entity.web;

import java.io.Serializable;

/**
 * 分页条件
 * Created by ljx on 2018/5/15.
 */
public class PageSearch implements Serializable {
    private int pageSize;
    private int pageNum;

    public PageSearch(int pageSize,int pageNum){
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }
    public PageSearch(){
        this.pageSize = 10;
        this.pageNum = 1;
    }
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
