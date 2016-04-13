package com.smart.framework.bean;

import com.smart.framework.base.BaseBean;
import java.util.List;

public class Pager<T> extends BaseBean
{
  private int pageNumber;
  private int pageSize;
  private int totalRecord;
  private List<T> recordList;

  public Pager(int pageNumber, int pageSize, int totalRecord, List<T> recordList)
  {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalRecord = totalRecord;
    this.recordList = recordList;
  }

  public int getTotalPage() {
    return this.totalRecord % this.pageSize == 0 ? this.totalRecord / this.pageSize : this.totalRecord / this.pageSize + 1;
  }

  public int getPageNumber() {
    return this.pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public int getPageSize() {
    return this.pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getTotalRecord() {
    return this.totalRecord;
  }

  public void setTotalRecord(int totalRecord) {
    this.totalRecord = totalRecord;
  }

  public List<T> getRecordList() {
    return this.recordList;
  }

  public void setRecordList(List<T> recordList) {
    this.recordList = recordList;
  }
}