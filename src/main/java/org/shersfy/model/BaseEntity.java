package org.shersfy.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSON;

public class BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date createTime;
	
	private Date updateTime;

	public Date getCreateTime() {
		return createTime;
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

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	
}
