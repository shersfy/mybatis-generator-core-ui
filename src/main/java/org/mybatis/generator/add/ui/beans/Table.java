package org.mybatis.generator.add.ui.beans;

import java.util.Map;


public class Table extends BaseBean{
	
	/**KEYS[0]:属性名，KEYS[1]:默认取值**/
	public static final String[] KEYS = new String[]{
			"enableCountByExample",
			"enableUpdateByExample",
			"enableDeleteByExample",
			"enableSelectByExample",
			"selectByExampleQueryId"
			//"enableUpdateByExample", 
			//"selectByPrimaryKeyQueryId"
	};
	
	
	private String tableName;
	
	private String domainObjectName;
	
	private Map<String, Object> properties;

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public String getTableName() {
		return tableName;
	}

	public String getDomainObjectName() {
		return domainObjectName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setDomainObjectName(String domainObjectName) {
		this.domainObjectName = domainObjectName;
	}
	
	
	
}
