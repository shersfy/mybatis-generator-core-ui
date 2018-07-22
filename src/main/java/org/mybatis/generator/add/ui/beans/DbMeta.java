package org.mybatis.generator.add.ui.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbMeta extends JdbcConnection{
	
	public static final String MYSQL 	= "MySQL";
	public static final String ORACLE 	= "Oracle";
	public static final String MSSQL 	= "MSSQL";
	
	private String typeCode;
	private String schema;
	
	public DbMeta() {
	}
	public DbMeta(String typeCode) {
		switch (typeCode) {
		case MYSQL:
			this.setDriverClass("com.mysql.jdbc.Driver");
			break;
		case ORACLE:
			this.setDriverClass("oracle.jdbc.driver.OracleDriver");
			break;
		case MSSQL:
			this.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			break;
		default:
			break;
		}
		this.typeCode = typeCode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getSchema() {
		if(schema != null){
			return schema;
		}
		switch (typeCode) {
		case MYSQL:
			break;
		case ORACLE:
			schema = getUserId()!=null?getUserId().toUpperCase():null;
			break;
		case MSSQL:
			schema = "DBO";
			break;
		default:
			break;
		}
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	@Override
	public String toString() {
		return this.typeCode;
	}
	
	/**
	 * 获取表名
	 * 
	 * @author PengYang
	 * @date 2016-10-26
	 * 
	 * @return List<String>
	 * @throws SQLException 
	 * @throws DatahubException
	 */
	public static List<String> getTables(DbMeta meta) throws SQLException {

		String url = meta.getConnectionURL();
		String user = meta.getUserId();
		String password = meta.getPassword();
		Connection conn = DriverManager.getConnection(url, user, password);
		List<String> list = new ArrayList<String>();

		ResultSet res = conn.getMetaData().getTables(conn.getCatalog(),
				meta.getSchema(), "%", new String[] { "TABLE" });
		while (res.next()) {
			list.add(res.getString("TABLE_NAME"));
		}
		conn.close();
		return list;

	}
	
}
