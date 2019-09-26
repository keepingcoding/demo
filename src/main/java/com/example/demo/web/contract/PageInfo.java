package com.example.demo.web.contract;

import java.io.Serializable;

/**
 * 分页Bean
 */
public class PageInfo implements Serializable {

    /** 一页显示条数 **/
    private int pageSize = 10;

    /** 当前页 **/
    private int pageNum = 1;

    /** 总记录数 **/
    private int totalRecord;

    /** 总页数 **/
    private int totalPage;

    /** 起始索引 会计算出来 **/
    private int startIndex;

    private static final long serialVersionUID = 1L;

    public int getStartIndex() {
        return startIndex = (this.getPageNum() - 1) * this.getPageSize();
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

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalPage() {
        return totalPage = (this.getTotalRecord() % this.getPageSize() == 0 ? (this.getTotalRecord() / this.getPageSize()) : (this.getTotalRecord() / this.getPageSize() + 1));
    }
}
