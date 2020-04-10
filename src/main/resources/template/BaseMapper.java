package org.shersfy.mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.shersfy.model.BaseEntity;

public interface BaseMapper<T extends BaseEntity, Id extends Serializable> {
	
	int insert(T entity);

	int deleteById(Id id);
	
	int deleteByIds(List<Id> ids);
	
	int updateById(T entity);
	
	T findById(Id id);
	
	long findListCount(Map<String, Object> map);
	
	List<T> findList(Map<String, Object> map);
	
}
