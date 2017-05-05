package com.mianbao.common;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by zoujiajian on 2017-5-4.
 */
public class Page<T> {

    public enum Order{
        asc, desc
    }

    private Integer page=1;				//当前页

    private Integer pagerNum=0;			//总页数

    private Long  	records=0L;			//数据记录数

    private Integer startRecord;		//开始条数

    private Integer pageSize=20;		//每页显示条数

    private List<T> rows;				//数据

    public Integer getPage() {
        return page;
    }


    public void setPage(Integer page) {
        if(page < 1){
            page=1;
        }
        this.page = page;
    }


    public Integer getPagerNum() {
        pagerNum = records.intValue() / pageSize;
        if (records % pageSize > 0) {
            pagerNum++;
        }
        return pagerNum;
    }


    public void setPagerNum(Integer pagerNum) {
        this.pagerNum = pagerNum;
    }


    public Long getRecords() {
        return records;
    }


    public void setRecords(Long records) {
        this.records = records;
    }

    public Integer getStartRecord() {
        return ( page - 1 ) * pageSize;
    }


    public void setStartRecord(Integer startRecord) {
        this.startRecord = startRecord;
    }


    public Integer getPageSize() {
        return pageSize;
    }


    public void setPageSize(Integer pageSize) {
        if(pageSize < 1){
            pageSize=1;
        }
        this.pageSize = pageSize;
    }


    public List<T> getRows() {
        return rows;
    }


    public void setRows(List<T> rows) {
        this.rows=rows;
    }


    @Override
    public String toString() {
        return "Pager [page=" + page + ", pagerNum=" + pagerNum
                + ", records=" + records + ", startRecord=" + startRecord
                + ", rows=" + rows + "]";
    }
}
