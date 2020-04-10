package org.mybatis.generator.add.ui.beans;

import org.apache.commons.lang.ClassUtils;

public class XmlMapperBean extends BaseBean{
	
	private String modelName;
	
	private String modelType;
	
	private String modelTypePK;
	
	public XmlMapperBean() {
		super();
	}
	

	public XmlMapperBean(String modelType, String modelTypePK) {
		super();
		this.modelName = ClassUtils.getShortClassName(modelType);
		this.modelType = modelType;
		this.modelTypePK = modelTypePK;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getModelTypePK() {
		return modelTypePK;
	}

	public void setModelTypePK(String modelTypePK) {
		this.modelTypePK = modelTypePK;
	}


}
