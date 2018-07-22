package org.mybatis.generator.add.ui;

import java.io.File;
import java.io.FileWriter;
import java.util.List;


import org.apache.log4j.Logger;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 添加findList方法
 * @author PengYang
 * @date 2016-10-27
 *
 * @copyright
 * Copyright Lenovo Corporation 2016 All Rights Reserved.
 */
public class AddMethodMapper {
	private static Logger logger = Logger.getLogger(AddMethodMapper.class);
	/**格式化文件**/
	public static void formatXml(){
		try {
			File file = new File(getMapperPath());
			File[] list = file.listFiles();
			for(File xml : list){
				createXml(xml);
			}
			logger.info("MyBatis Generator finished successfully.");
		} catch (Exception e) {
			logger.error(e);
		}
	}
	/**清除文件**/
	public static void clearFile(){
		try {
			File file = new File(getMapperPath());
			if(!file.isDirectory()){
				file.mkdirs();
			}
			File[] list = file.listFiles();
			for(File xml : list){
				xml.delete();
				logger.info(xml.getName()+"删除");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void createXml(File xmlFile){

		try {  

			//1。获取DOM 解析器的工厂实例。  
			SAXReader reader = new SAXReader(); 
			//2。获得具体的DOM解析器。  
			//DocumentBuilder builder = factory.newDocumentBuilder();  
			//3。获取文件  
			Document document = reader.read(xmlFile);
			//Document extDoc = DocumentHelper.createDocument();
			//4。获取根元素  
			Element root = document.getRootElement();
			//5。获取节点[有多个节点]
			
			List<Element> list = root.elements("resultMap");
			Element resMapNode = list.get(0);
			String paramType = resMapNode.attributeValue("type");
			String[] arr = paramType .split("[.]");
			paramType = arr[arr.length-1];

			// select start
			Element includeColumn = DocumentHelper.createElement("include");
			includeColumn.addAttribute("refid", "Base_Column_List");

			Element choose = DocumentHelper.createElement("choose");
			Element when = DocumentHelper.createElement("when");
			when.addAttribute("test", "(sort !=null and sort !='') and (order !=null and order !='')");
			when.setText("order by ${sort} ${order}");
			Element otherwise = DocumentHelper.createElement("otherwise");
			otherwise.setText("order by id asc");
			choose.add(when);
			choose.add(otherwise);

			Element if1 = DocumentHelper.createElement("if");
			if1.addAttribute("test", "startIndex !=null  and pageSize !=null");
			if1.setText("limit #{startIndex}, #{pageSize}");

			String table = camelToUnderline(paramType);

			Element select = DocumentHelper.createElement("select");
			select.addAttribute("id", "findList");
			select.addAttribute("resultMap", "BaseResultMap");
			select.addAttribute("parameterType", paramType);

			select.add(DocumentHelper.createText("select"));
			select.add(includeColumn);
			select.add(DocumentHelper.createText("from "+table));
			Element includeCondition = DocumentHelper.createElement("include");
			includeCondition.addAttribute("refid", "condition");
			select.add(includeCondition);
			select.add(choose);
			select.add(if1);
			// select end

			// select findListCount start
			Element selectCount = DocumentHelper.createElement("select");
			selectCount.addAttribute("id", "findListCount");
			selectCount.addAttribute("resultType", "Long");
			selectCount.addAttribute("parameterType", paramType);

			selectCount.add(DocumentHelper.createText("select count(1) from "+table));
			includeCondition = DocumentHelper.createElement("include");
			includeCondition.addAttribute("refid", "condition");
			selectCount.add(includeCondition);
			// select findListCount end

			// sql start
			Element sqlNode = DocumentHelper.createElement("sql");
			sqlNode.addAttribute("id", "condition");

			Element sqlWhereNode = DocumentHelper.createElement("where");
			List<Element> list2 = resMapNode.elements("result");
			for(int i=0; i<list2.size(); i++){
				Element sqlIfNode = DocumentHelper.createElement("if");
				Element item = list2.get(i);
				sqlIfNode.addAttribute("test", item.attributeValue("property")+" != null");

				StringBuffer txt = new StringBuffer(0);
				txt.append("and ");
				txt.append(item.attributeValue("column"));
				txt.append(" = ");
				txt.append("#{");
				txt.append(item.attributeValue("property"));
				txt.append(", jdbcType=");
				txt.append(item.attributeValue("jdbcType"));
				txt.append("}");
				sqlIfNode.setText(txt.toString());
				sqlWhereNode.add(sqlIfNode);
			}
			sqlNode.add(sqlWhereNode);
			// sql end
			
			// insert start
			Element insertNode = (Element) root.elements("insert").get(0);
			insertNode.addAttribute("parameterType", paramType);
			insertNode.addAttribute("keyProperty", "id");
			insertNode.addAttribute("useGeneratedKeys", "true");
			// insert end

			//root
			//Element extRoot = DocumentHelper.createElement("mapper");
			root.remove(insertNode);
			root.add(insertNode);
			root.add(select);
			root.add(selectCount);
			root.add(sqlNode);
			
			// 输出不需要的节点
			removeNode(root);
			// 删除注释节点
			removeComment(root);
			
			document.setRootElement(root);
			// 可以使用 addDocType()方法添加文档类型说明。
			//extDoc.addDocType("mapper", "-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
			
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
			logger.debug(document.asXML());
			
			fw.write(document);
			fw.flush();
			fw.close();
			logger.info(xmlFile.getPath()+"创建完成");
		} catch (Exception e) { 
			logger.error(e);
		} 

	}

	/**驼峰转下横线**/
	public static String camelToUnderline(String param){  
		if (param==null||"".equals(param.trim())){  
			return "";  
		}  
		int len=param.length();  
		StringBuilder sb=new StringBuilder(len); 
		sb.append(Character.toLowerCase(param.charAt(0)));
		for (int i = 1; i < len; i++) {  
			char c=param.charAt(i);  
			if (Character.isUpperCase(c)){  
				sb.append("_");
				sb.append(Character.toLowerCase(c));  
			}else{  
				sb.append(c);  
			}  
		}  
		return sb.toString();
	}
	/**下横线转驼峰**/
	public static String underlineToCamel(String param){  
		if (param==null||"".equals(param.trim())){  
			return "";  
		}  
		int len=param.length();  
		StringBuilder sb=new StringBuilder(len);
		sb.append(Character.toUpperCase(param.charAt(0)));
		for (int i = 1; i < len; i++) {  
			char c=param.charAt(i);  
			if (c=='_'){  
				if (++i<len){  
					sb.append(Character.toUpperCase(param.charAt(i)));  
				}  
			}else{  
				sb.append(c);  
			}  
		}  
		return sb.toString();
	}  

	public static String getMapperPath() throws Exception{
		StringBuffer path = new StringBuffer(0);
		//1。获取DOM 解析器的工厂实例。  
		SAXReader reader = new SAXReader(); 
		//2。获得具体的DOM解析器。  
		//DocumentBuilder builder = factory.newDocumentBuilder();  
		//3。获取文件  
		Document document = reader.read(ClassLoader.getSystemResource("generatorConfig.xml"));
		//4。获取根元素  
		Element root = document.getRootElement();
		
		//5。获取节点[有多个节点]
		Element mapperNode = root.element("context").element("sqlMapGenerator");
		path.append(mapperNode.attributeValue("targetProject").replaceAll("[.]", "/"));
		path.append("/");
		path.append(mapperNode.attributeValue("targetPackage").replaceAll("[.]", "/"));
		path.append("/");
		return path.toString();
	}
	
	@SuppressWarnings("unchecked")
	private static void removeNode(Element node){
		List<Element> list = node.elements();
		for(Element e:list){
			String id = e.attributeValue("id");
			if("insertSelective".equals(id)
				|| "updateByPrimaryKey".equals(id)
				|| "updateByPrimaryKeyWithBLOBs".equals(id)){
				node.remove(e);
			}
		}
	}
	/**删除xml中的注释**/
	private static void removeComment(Element node){
		List<?> list = node.content();
		for(Object e:list){
			if(e instanceof Comment){
				Comment c = (Comment)e;
				node.remove(c);
			}
			else if(e instanceof Element){
				Element el = (Element)e;
				removeComment(el);
			}
		}
	}
	
}
