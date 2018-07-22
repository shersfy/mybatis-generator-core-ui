package org.mybatis.generator.add.ui.beans;

public class JdbcConnection extends BaseBean{

	
	private String driverClass;
	private String connectionURL;
	private String userId;
	private String password;
	
	public JdbcConnection(String driverClass, String connectionURL) {
		super();
		this.driverClass = driverClass;
		this.connectionURL = connectionURL;
	}
	
	public JdbcConnection() {
		super();
	}

	public JdbcConnection(String driverClass, String connectionURL, String userId, String password) {
		super();
		this.driverClass = driverClass;
		this.connectionURL = connectionURL;
		this.userId = userId;
		this.password = password;
	}

	public String getDriverClass() {
		return driverClass;
	}


	public String getConnectionURL() {
		return connectionURL;
	}
	public String getUserId() {
		return userId;
	}
	public String getPassword() {
		return password;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
