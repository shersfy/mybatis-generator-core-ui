package org.mybatis.generator.add.ui.beans;

import com.alibaba.fastjson.JSON;

public class BaseBean {

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	

}
