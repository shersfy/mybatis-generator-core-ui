package org.shersfy.service;

import java.io.Serializable;
import java.util.List;

import org.young.commons.beans.Page;
import org.young.commons.beans.Result.ResultCode;
import org.shersfy.model.BaseEntity;

public interface BaseService<T extends BaseEntity, Id extends Serializable> {
	
	int SUCESS = ResultCode.SUCESS;
	int FAIL   = ResultCode.FAIL;
	
	int NOR    = 0;
	int DEL    = 1;
	int TMP    = -1;
	
	<Vo extends T> Vo poToVo(T po, Class<Vo> vclass);
	
	int deleteById(Id id);
	
	int deleteByIds(List<Id> ids);

	int save(Id id, T entity);
	
	int insert(T entity);

	T findById(Id id);
	
	int updateById(T entity);
	
	long findListCount(T where);
	
	List<T> findList(T where);

	Page<T> findPage(T where, int pageNo, int pageSize);
	
	Page<T> findPage(T where, String keyword, int pageNo, int pageSize);

	String getCacheKey(String prefix);

	String getCacheKey(String methodName, String suffix);

}
