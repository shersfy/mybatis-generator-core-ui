package org.mybatis.generator.add.ui.beans;

public class ContextBean extends BaseBean{
	
	private CommentGenerator commentGenerator;
	private JdbcConnection jdbcConnection;
	private JavaTypeResolver javaTypeResolver;
	private JavaModelGenerator javaModelGenerator;
	private SqlMapGenerator sqlMapGenerator;
	private JavaClientGenerator javaClientGenerator;
	private Table[] tables;
	
	public ContextBean(){
	}
	
	public CommentGenerator getCommentGenerator() {
		return commentGenerator;
	}
	public JdbcConnection getJdbcConnection() {
		return jdbcConnection;
	}
	public JavaTypeResolver getJavaTypeResolver() {
		return javaTypeResolver;
	}
	public JavaModelGenerator getJavaModelGenerator() {
		return javaModelGenerator;
	}
	public SqlMapGenerator getSqlMapGenerator() {
		return sqlMapGenerator;
	}
	public JavaClientGenerator getJavaClientGenerator() {
		return javaClientGenerator;
	}
	public Table[] getTables() {
		return tables;
	}
	public void setCommentGenerator(CommentGenerator commentGenerator) {
		this.commentGenerator = commentGenerator;
	}
	public void setJdbcConnection(JdbcConnection jdbcConnection) {
		this.jdbcConnection = jdbcConnection;
	}
	public void setJavaTypeResolver(JavaTypeResolver javaTypeResolver) {
		this.javaTypeResolver = javaTypeResolver;
	}
	public void setJavaModelGenerator(JavaModelGenerator javaModelGenerator) {
		this.javaModelGenerator = javaModelGenerator;
	}
	public void setSqlMapGenerator(SqlMapGenerator sqlMapGenerator) {
		this.sqlMapGenerator = sqlMapGenerator;
	}
	public void setJavaClientGenerator(JavaClientGenerator javaClientGenerator) {
		this.javaClientGenerator = javaClientGenerator;
	}
	public void setTables(Table[] tables) {
		this.tables = tables;
	}

}
