package org.shersfy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

%s
import org.shersfy.mapper.BaseMapper;
import org.shersfy.mapper.TemplateMapper;
import org.shersfy.service.TemplateService;

@Service
@Transactional
public class TemplateServiceImpl extends BaseServiceImpl<%s> 
	implements TemplateService {
	
	@Autowired
	private TemplateMapper mapper;

	@Override
	public BaseMapper<%s> getMapper() {
		return mapper;
	}
	
}
