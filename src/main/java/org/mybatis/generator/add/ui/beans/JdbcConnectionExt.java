package org.mybatis.generator.add.ui.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcConnectionExt extends JdbcConnection{
	
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
	public static List<String> getTables(JdbcConnectionExt meta) throws SQLException {

		String url = meta.getConnectionURL();
		String user = meta.getUserId();
		String password = meta.getPassword();
		Connection conn = DriverManager.getConnection(url, user, password);
		List<String> list = new ArrayList<String>();

		ResultSet res = conn.getMetaData().getTables(conn.getCatalog(),
				conn.getSchema(), "%", new String[] { "TABLE" });
		while (res.next()) {
			list.add(res.getString("TABLE_NAME"));
		}
		conn.close();
		return list;

	}
	
}
