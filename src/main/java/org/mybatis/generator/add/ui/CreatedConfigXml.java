package org.mybatis.generator.add.ui;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.mybatis.app.boot.BootApplication;
import org.mybatis.generator.add.ui.beans.CommentGenerator;
import org.mybatis.generator.add.ui.beans.ContextBean;
import org.mybatis.generator.add.ui.beans.JavaClientGenerator;
import org.mybatis.generator.add.ui.beans.JavaModelGenerator;
import org.mybatis.generator.add.ui.beans.JavaTypeResolver;
import org.mybatis.generator.add.ui.beans.JdbcConnection;
import org.mybatis.generator.add.ui.beans.SqlMapGenerator;
import org.mybatis.generator.add.ui.beans.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 生成配置文件
 * @author Young
 * @date 2016-12-21
 *
 */
public class CreatedConfigXml {
	
	public static final String CONF_XML = BootApplication.CONF_XML;
	
	private static Logger logger = LoggerFactory.getLogger(CreatedConfigXml.class);
	
	public static File createConfigXml(ContextBean context){
		File xmlFile = new File(ClassLoader.getSystemResource("./").getPath(), CONF_XML);
		
		try {
			Document doc = DocumentHelper.createDocument();
			doc.addDocType("generatorConfiguration",
					"-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN", 
					"http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd");

			Element root = DocumentHelper.createElement("generatorConfiguration");
			Element ctxt = DocumentHelper.createElement("context");
			ctxt.addAttribute("id", "DB2Tables");
			ctxt.addAttribute("targetRuntime", "MyBatis3");
			
			ctxt.add(DocumentHelper.createComment("是否去除自动生成的注释。 true：去除 ; false:保留"));
			Element commentGenerator = DocumentHelper.createElement("commentGenerator");
			addPropertys(commentGenerator, context.getCommentGenerator().toString());
			ctxt.add(commentGenerator);
			
			ctxt.add(DocumentHelper.createComment("数据库链接URL，用户名、密码"));
			Element jdbcConnection = DocumentHelper.createElement("jdbcConnection");
			addAttributes(jdbcConnection, context.getJdbcConnection().toString());
			ctxt.add(jdbcConnection);
			
			Element javaTypeResolver = DocumentHelper.createElement("javaTypeResolver");
			addPropertys(javaTypeResolver, context.getJavaTypeResolver().toString());
			ctxt.add(javaTypeResolver);
			
			ctxt.add(DocumentHelper.createComment("生成model的包名和位置, rootClass指定继承的基类"));
			Element javaModelGenerator = DocumentHelper.createElement("javaModelGenerator");
			javaModelGenerator.addAttribute("targetPackage", context.getJavaModelGenerator().getTargetPackage());
			javaModelGenerator.addAttribute("targetProject", context.getJavaModelGenerator().getTargetProject());
			addProperty(javaModelGenerator, "enableSubPackages", context.getJavaModelGenerator().isEnableSubPackages());
			addProperty(javaModelGenerator, "trimStrings", context.getJavaModelGenerator().isTrimStrings());
			if(!StringUtils.isBlank(context.getJavaModelGenerator().getRootClass())){
				addProperty(javaModelGenerator, "rootClass", context.getJavaModelGenerator().getRootClass());
			}
			ctxt.add(javaModelGenerator);
			
			ctxt.add(DocumentHelper.createComment("生成映射文件的包名和位置"));
			Element sqlMapGenerator = DocumentHelper.createElement("sqlMapGenerator");
			sqlMapGenerator.addAttribute("targetPackage", context.getSqlMapGenerator().getTargetPackage());
			sqlMapGenerator.addAttribute("targetProject", context.getSqlMapGenerator().getTargetProject());
			addProperty(sqlMapGenerator, "enableSubPackages", context.getSqlMapGenerator().isEnableSubPackages());
			ctxt.add(sqlMapGenerator);
			
			ctxt.add(DocumentHelper.createComment("生成DAO的包名和位置"));
			Element javaClientGenerator = DocumentHelper.createElement("javaClientGenerator");
			javaClientGenerator.addAttribute("type", context.getJavaClientGenerator().getType());
			javaClientGenerator.addAttribute("targetPackage", context.getJavaClientGenerator().getTargetPackage());
			javaClientGenerator.addAttribute("targetProject", context.getJavaClientGenerator().getTargetProject());
			addProperty(javaClientGenerator, "enableSubPackages", context.getJavaClientGenerator().isEnableSubPackages());
			ctxt.add(javaClientGenerator);
			
			// tables
			ctxt.add(DocumentHelper.createComment("要生成哪些表"));
			Table[] tbls = context.getTables();
			if(tbls != null){
				for(Table tbl : tbls){
					Element table = DocumentHelper.createElement("table");
					table.addAttribute("tableName", tbl.getTableName());
					table.addAttribute("domainObjectName", tbl.getDomainObjectName());
					addAttributes(table, JSON.toJSONString(tbl.getProperties()));
					ctxt.add(table);
				}
			}
			
			root.add(ctxt);
			doc.setRootElement(root);

			OutputFormat xmlFormat = new OutputFormat();
			//设置文件编码 
			xmlFormat.setEncoding("UTF-8");
			// 设置换行
			xmlFormat.setNewlines(true);
			// 生成缩进
			xmlFormat.setIndent(true);
			// 使用4个空格进行缩进
			xmlFormat.setIndentSize(4);
			
			XMLWriter fw = new XMLWriter(new FileWriter(xmlFile), xmlFormat);
			logger.debug(fw.toString());

			fw.write(doc);
			fw.flush();
			fw.close();
			logger.info("create config xml '{}' successful", xmlFile.getPath());

		} catch (Exception e) {
			logger.error("create config xml error", e);
		}
		
		return xmlFile;
	}
	
	public static void main(String[] args) {
		ContextBean cb = new ContextBean();
		
		String outpath = "E:/data/datax";
		
		CommentGenerator commentGenerator = new CommentGenerator();
		commentGenerator.setSuppressAllComments(true);
		commentGenerator.setSuppressDate(true);
		cb.setCommentGenerator(commentGenerator );
		
		
		cb.setJavaTypeResolver(new JavaTypeResolver(false));
		
		JdbcConnection jdbcConnection = new JdbcConnection();
		jdbcConnection.setConnectionURL("jdbc:oracle:thin:@192.168.100.231:1521:orcl");
		jdbcConnection.setDriverClass("oracle.jdbc.driver.OracleDriver");
		jdbcConnection.setUserId("yuehantest");
		jdbcConnection.setPassword("yuehantest");
		cb.setJdbcConnection(jdbcConnection);
		
		JavaModelGenerator javaModelGenerator = new JavaModelGenerator();
		javaModelGenerator.setEnableSubPackages(true);
		javaModelGenerator.setRootClass("com.lenovo.datahub.domain.BaseEntity");
		javaModelGenerator.setTargetPackage("com.lenovo.datahub.domain");
		javaModelGenerator.setTargetProject(outpath);
		javaModelGenerator.setTrimStrings(false);
		cb.setJavaModelGenerator(javaModelGenerator);
		
		SqlMapGenerator sqlMapGenerator = new SqlMapGenerator();
		sqlMapGenerator.setEnableSubPackages(true);
		sqlMapGenerator.setTargetPackage("mapper");
		sqlMapGenerator.setTargetProject(outpath);
		cb.setSqlMapGenerator(sqlMapGenerator);
		
		JavaClientGenerator javaClientGenerator = new JavaClientGenerator();
		javaClientGenerator.setEnableSubPackages(true);
		javaClientGenerator.setTargetPackage("com.lenovo.datahub.dao");
		javaClientGenerator.setTargetProject(outpath);
		javaClientGenerator.setType("XMLMAPPER");
		cb.setJavaClientGenerator(javaClientGenerator );
		
		
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("enableCountByExample", false);
		properties.put("enableUpdateByExample", false);
		properties.put("enableDeleteByExample", false);
		properties.put("enableSelectByExample", false);
		properties.put("selectByExampleQueryId", false);
		
		Table[] tables = new Table[2];
		tables[0] = new Table();
		tables[0].setTableName("job_log");
		tables[0].setDomainObjectName("JobLog");
		tables[0].setProperties(properties);
		
		tables[1] = new Table();
		tables[1].setTableName("kettest");
		tables[1].setDomainObjectName("Kettest");
		tables[1].setProperties(properties);
		
		cb.setTables(tables);
		
		createConfigXml(cb );
	}
	
	private static Element addAttributes(Element e, String jsonStr){
		
		JSONObject json = JSON.parseObject(jsonStr);
		if(json == null){
			return e;
		}
		for(Entry<String, Object> en : json.entrySet()){
			e.addAttribute(en.getKey(), String.valueOf(en.getValue()));
		}
		
		return e;
	}
	
	private static Element addPropertys(Element e, String jsonStr){
		
		JSONObject json = JSON.parseObject(jsonStr);
		for(Entry<String, Object> en : json.entrySet()){
			Element prop = DocumentHelper.createElement("property");
			prop.addAttribute("name", en.getKey());
			prop.addAttribute("value", String.valueOf(en.getValue()));
			e.add(prop);
		}
		
		return e;
	}
	
	private static Element addProperty(Element e, String key, Object value){
		
		Element prop = DocumentHelper.createElement("property");
		prop.addAttribute("name", key);
		prop.addAttribute("value", String.valueOf(value));
		e.add(prop);
		
		return e;
	}
	
}
