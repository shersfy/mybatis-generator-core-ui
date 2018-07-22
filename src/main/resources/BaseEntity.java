package com.lenovo.datahub.domain;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

/**
 * 基实体
 * @author PengYang
 * @date 2016-08-26
 *
 */
public class BaseEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 分页开始索引**/
	private Integer startIndex;
	
	/** 分页大小**/
	private Integer pageSize;
	
	/** 排序字段 **/
	private String sort;
	
	/** 排序方式 **/
	private String order;

	/** 创建时间 **/
    private Date createTime;

    /** 更新时间 **/
    private Date updateTime;
    
    /** 开始时间 **/
    private Date startTime;
    /** 结束时间 **/
    private Date endTime;

	public Date getCreateTime() {
		return createTime;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取排序字段
	 * 
	 * @author PengYang
	 * @date 2016-08-29
	 * 
	 * @return
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * 设置排序字段
	 * 
	 * @author PengYang
	 * @date 2016-08-29
	 * 
	 * @param sort
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * 获取排序方式
	 * 
	 * @author PengYang
	 * @date 2016-08-29
	 * 
	 * @return
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 设置排序方式(ASC|DESC)
	 * 
	 * @author PengYang
	 * @date 2016-08-29
	 * 
	 * @param order
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
    
}
