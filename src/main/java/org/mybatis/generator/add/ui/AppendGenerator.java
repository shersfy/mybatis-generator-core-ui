package org.mybatis.generator.add.ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.mybatis.app.boot.BootApplication;
import org.mybatis.generator.add.ui.beans.ContextBean;
import org.mybatis.generator.add.ui.beans.XmlMapperBean;
import org.mybatis.generator.internal.util.CamelCaseUtils;
import org.shersfy.model.BaseEntity;
import org.shersfy.utils.LocalConfig;
import org.shersfy.utils.SAXReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 增强处理
 * @author Young
 * @date 2020-04-10
 */
public class AppendGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppendGenerator.class);
	private static final int MAXLEN = 100;
	
	/**
	 * 执行前
	 */
	public static void runBefore() {
		LOGGER.info("========== generate start ==========");
		LOGGER.info("shell run before start");
		clearFile();
		LOGGER.info("shell run before finished");
	}

	/**
	 * 执行后
	 */
	public static void runAfter() {
		LOGGER.info("shell run after start");
		//格式化生成的XML文件
		int cnt = formatXml();
		if (cnt!=0) {
			// 拷贝基类
			copyBaseClass();
			// 调整java文件
			formatJava();
		}
		LOGGER.info("shell run after finished");
		LOGGER.info("========== generate finish ==========");
	}

	/**格式化文件**/
	public static int formatXml(){
		try {
			File file = new File(getXmlMappingPath());
			File[] list = file.listFiles();
			for(File xml : list){
				generateXml(xml);
			}
			LOGGER.info("MyBatis Generator finished successfully.");
			return list.length;
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return 0;
	}

	public static void copyBaseClass() {

		File basedir = LocalConfig.getBasedir();
		File baseEntity = new File(basedir, "template/BaseEntity.java");
		File baseMapper = new File(basedir, "template/BaseMapper.java");
		File baseService = new File(basedir, "template/BaseService.java");
		File baseServiceImpl = new File(basedir, "template/BaseServiceImpl.java");

		//baseEntity
		if (baseEntity.isFile()) {
			copyFile(baseEntity, getModelPath());
		}

		//baseMapper
		if (baseMapper.isFile()) {
			copyFile(baseMapper, getMapperPath());
		}

		//baseService
		File model = new File(getModelPath());
		if (baseService.isFile()) {
			copyFile(baseService, model.getParent()+"/service");
		}

		//baseServiceImpl
		if (baseServiceImpl.isFile()) {
			copyFile(baseServiceImpl, model.getParent()+"/service/impl");
		}

	}

	public static void formatJava() {
		formatJavaModel();
		generateJavaMapper();
	}

	private static void copyFile(File srcFile, String destPath) {
		try {
			File destFile = new File(destPath, srcFile.getName());
			LOGGER.info("copy {}==>{}", srcFile.getAbsolutePath(), destFile.getAbsolutePath());
			FileUtils.copyFile(srcFile, destFile);
			replacePackage(destFile);
		} catch (IOException e) {
			LOGGER.error("", e);
		}

	}

	private static void formatJavaModel() {

		File dir = new File(getModelPath());
		Collection<File> files = FileUtils.listFiles(dir, null, false);

		for (File file :files) {
			if (file.getName().equals("BaseEntity.java")) {
				continue;
			}

			LOGGER.info("formart java file: {}", file.getAbsolutePath());
			try {
				List<String> data  = new ArrayList<>();
				List<String> lines = FileUtils.readLines(file);
				String preline = null;
				for (String line :lines) {
					// 跳过连续空行
					if (StringUtils.isBlank(preline) && StringUtils.isBlank(line)) {
						continue;
					}
					// 删除 import org.shersfy.model.BaseEntity;
					if (line.contains(BaseEntity.class.getName())) {
						continue;
					}
					preline = line;
					data.add(line);

					// 添加 private static final long serialVersionUID = 1L;
					if (line.startsWith("public class")) {

						data.add(StringUtils.EMPTY);
						data.add("	private static final long serialVersionUID = 1L;");
						data.add(StringUtils.EMPTY);
					}
				}

				FileUtils.writeLines(file, data);
			} catch (IOException e) {
				LOGGER.error("", e);
			}

		}
	}

	public static void generateJavaMapper() {

		// view TemplateServiceImpl.java
		String template = null;
		try {
			File basedir = LocalConfig.getBasedir();
			File temp = new File(basedir, "template/TemplateServiceImpl.java");
			template = FileUtils.readFileToString(temp);
		} catch (IOException e) {
			LOGGER.error("", e);
			return;
		};

		File dir = new File(getMapperPath());
		Collection<File> files = FileUtils.listFiles(dir, null, false);
		for (File file :files) {
			if (file.getName().equals("BaseMapper.java")) {
				continue;
			}

			LOGGER.info("generate mapper {}", file.getAbsolutePath());
			String modelName = FilenameUtils.getBaseName(file.getName());
			modelName = modelName.substring(0, modelName.lastIndexOf("Mapper"));
			try {
				XmlMapperBean xml = getXmlMapperBean(modelName);
				generateMapper(file, xml);
			} catch (Exception e) {
				LOGGER.error("", e);
			}

			// 生成对应的service
			File model = new File(getModelPath(), modelName+".java");
			try {
				XmlMapperBean xml = getXmlMapperBean(modelName);
				generateJavaService(model);
				generateJavaServiceImpl(model, xml, template);
			} catch (Exception e) {
				LOGGER.error("", e);
			}

		}

	}

	private static XmlMapperBean getXmlMapperBean(String modelName) 
			throws DocumentException, IOException {
		File xml = new File(getXmlMappingPath(), String.format("%sMapper.xml", modelName));

		Document document = SAXReaderUtil.getDocument(xml);
		Element root = document.getRootElement();

		Element updateById = null;
		Element deleteById = null;

		List<?> list = root.elements("update");
		for (Object e : list) {
			Element el = (Element)e;
			if ("updateById".equals(el.attributeValue("id"))) {
				updateById = el;
				break;
			}
		}

		list = root.elements("delete");
		for (Object e : list) {
			Element el = (Element)e;
			if ("deleteById".equals(el.attributeValue("id"))) {
				deleteById = el;
				break;
			}
		}

		if (updateById==null) {
			throw new IOException("updateById element not exists");
		}

		if (deleteById==null) {
			throw new IOException("deleteById element not exists");
		}

		String modelType = updateById.attributeValue("parameterType");
		String modelTypePK = deleteById.attributeValue("parameterType");
		XmlMapperBean bean = new XmlMapperBean(modelType, modelTypePK);

		return bean;
	}

	private static void generateMapper(File mapper, XmlMapperBean xml) throws IOException {
		List<String> lines = new ArrayList<>();
		// package
		String pkg = BootApplication.getContextBean().getJavaClientGenerator().getTargetPackage();
		lines.add(String.format("package %s;", pkg));
		lines.add(StringUtils.EMPTY);

		// import
		lines.add(String.format("import %s;", xml.getModelType()));
		if (!xml.getModelTypePK().startsWith("java.lang")) {
			lines.add(String.format("import %s;", xml.getModelTypePK()));
		}
		lines.add(StringUtils.EMPTY);

		// public interface XxxMapper
		String str1 = String.format("public interface %sMapper ", xml.getModelName());
		// extends BaseMapper<Xxx, XxxKey>{
		String str2 = String.format("extends BaseMapper<%s, %s>{", xml.getModelName(),
				ClassUtils.getShortClassName(xml.getModelTypePK()));
		if ((str1+str2).length()>MAXLEN) {
			lines.add(str1);
			lines.add(str2);
		} else {
			lines.add(str1+str2);
		}
		
		lines.add("\t");
		lines.add("}");

		FileUtils.writeLines(mapper, lines);
	}

	public static void generateJavaService(File model) throws Exception {

		String parent = model.getParentFile().getParent();
		String modelName = FilenameUtils.getBaseName(model.getName());
		XmlMapperBean xml = getXmlMapperBean(modelName);

		File service = new File(parent, String.format("service/%sService.java", modelName));
		String servicePkg  = getModelParentPkg()+".service";
		
		LOGGER.info("generate service {}", service.getAbsolutePath());

		List<String> lines = new ArrayList<>();
		// package
		lines.add(String.format("package %s;", servicePkg));
		lines.add(StringUtils.EMPTY);

		// import
		lines.add(String.format("import %s;", xml.getModelType()));
		if (!xml.getModelTypePK().startsWith("java.lang")) {
			lines.add(String.format("import %s;", xml.getModelTypePK()));
		}
		lines.add(StringUtils.EMPTY);

		// public interface XxxService
		String str1 = String.format("public interface %sService ", xml.getModelName());
		// extends BaseService<Xxx, XxxKey>{
		String str2 = String.format("extends BaseService<%s, %s>{", xml.getModelName(),
				ClassUtils.getShortClassName(xml.getModelTypePK()));
		if ((str1+str2).length()>MAXLEN) {
			lines.add(str1);
			lines.add(str2);
		} else {
			lines.add(str1+str2);
		}
		
		lines.add("\t");
		lines.add("}");

		FileUtils.writeLines(service, lines);

	}

	public static void generateJavaServiceImpl(File model, 
			XmlMapperBean xml, String template) throws IOException {
		
		String parent = model.getParentFile().getParent();
		String modelName = FilenameUtils.getBaseName(model.getName());

		File java = new File(parent, String.format("service/impl/%sServiceImpl.java", modelName));
		String mapperPkg = BootApplication.getContextBean().getJavaClientGenerator().getTargetPackage();
		String targetPkg = getModelParentPkg()+".service";

		LOGGER.info("generate service implement {}", java.getAbsolutePath());
		
		// package
		String data = template.replace("org.shersfy.mapper", mapperPkg);
		data = data.replace("org.shersfy.service", targetPkg);
		// TemplateMapper
		data = data.replace("TemplateMapper", modelName+"Mapper");
		// TemplateService
		data = data.replace("TemplateService", modelName+"Service");

		// import
		StringBuffer impt = new StringBuffer();
		impt.append(String.format("import %s;", xml.getModelType()));
		if (!xml.getModelTypePK().startsWith("java.lang")) {
			impt.append("\n");
			impt.append(String.format("import %s;", xml.getModelTypePK()));
		}
		
		// type
		String type = String.format("%s, %s", modelName,
				ClassUtils.getShortClassName(xml.getModelTypePK()));
		// 3个参数替换
		data = String.format(data, impt, type, type);
		FileUtils.write(java, data);
	}
	
	
	public static void generateJavaController(File model, 
			XmlMapperBean xml, String template) throws IOException {
		
		String parent = model.getParentFile().getParent();
		String modelName = FilenameUtils.getBaseName(model.getName());

		File java = new File(parent, String.format("controller/%sController.java", modelName));
		String parentPkg = getModelParentPkg();
		String targetPkg = parentPkg+".controller";
		
		String service = String.format("%s.service.%s", modelName+"Service");

		LOGGER.info("generate controller {}", java.getAbsolutePath());
		
		// package
		String data = template.replace("org.shersfy.controller", targetPkg);
		// TemplateService
		data = data.replace("TemplateService", ClassUtils.getShortClassName(service));

		// import
		StringBuffer impt = new StringBuffer();
		impt.append(String.format("import %s;", xml.getModelType()));
		if (!xml.getModelTypePK().startsWith("java.lang")) {
			impt.append("\n");
			impt.append(String.format("import %s;", xml.getModelTypePK()));
		}
		impt.append(String.format("import %s;", service));
		
		String path = CamelCaseUtils.toSeparatorString(modelName, "/");
		// pktype
		String pktype = ClassUtils.getShortClassName(xml.getModelTypePK());

		// 4个参数替换
		data = String.format(data, impt, path, pktype, pktype);
		FileUtils.write(java, data);
	}

	public static void replacePackage(File file) throws IOException {
		// replace package
		String oldmodel = BaseEntity.class.getPackage().getName();
		String oldparent= oldmodel.substring(0, oldmodel.lastIndexOf("."));

		String newmodel = BootApplication.getContextBean().getJavaModelGenerator().getTargetPackage();
		String newparent= newmodel.substring(0, newmodel.lastIndexOf("."));

		String data = FileUtils.readFileToString(file);
		data = data.replace(oldmodel, newmodel);
		data = data.replace(oldparent+".mapper", newparent+".mapper");
		data = data.replace(oldparent+".service", newparent+".service");

		FileUtils.write(file, data);
	}

	/**清除文件**/
	public static void clearFile(){
		try {
			File output = new File(BootApplication.getContextBean()
					.getSqlMapGenerator().getTargetProject());
			if(!output.isDirectory()){
				output.mkdirs();
			}
			File[] list = output.listFiles();
			for(File file : list){
				LOGGER.info("delete {}", file.getAbsolutePath());
				FileUtils.deleteQuietly(file);
			}
		} catch (Exception e) {
			LOGGER.info("", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static void generateXml(File xml){

		try {
			Document document = SAXReaderUtil.getDocument(xml);
			Element root = document.getRootElement();

			List<Element> list = root.elements("resultMap");
			Element resMapNode = list.get(0);
			String paramType   = resMapNode.attributeValue("type");

			// select findList start
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

			String table = CamelCaseUtils.toUnderlineString(ClassUtils.getShortClassName(paramType));

			Element select = DocumentHelper.createElement("select");
			select.addAttribute("id", "findList");
			select.addAttribute("resultMap", "BaseResultMap");
			select.addAttribute("parameterType", paramType);

			select.add(DocumentHelper.createText("select "));
			select.add(includeColumn);
			select.add(DocumentHelper.createText(" from "+table));
			Element includeCondition = DocumentHelper.createElement("include");
			includeCondition.addAttribute("refid", "condition");
			select.add(includeCondition);
			select.add(choose);
			select.add(if1);
			// select findList end

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

			// sql condition start
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
			// sql condition end

			// insert start
			Element insertNode = (Element) root.elements("insert").get(0);
			insertNode.addAttribute("parameterType", paramType);
			insertNode.addAttribute("keyProperty", "id");
			insertNode.addAttribute("useGeneratedKeys", "true");
			// insert end
			
			// delete deleteByIds start
			Element deleteByIds = DocumentHelper.createElement("delete");
			deleteByIds.addAttribute("id", "deleteByIds");
			deleteByIds.addAttribute("parameterType", "list");
			deleteByIds.add(DocumentHelper.createText(String.format("delete from %s where id in ", table)));

			Element foreach = DocumentHelper.createElement("foreach");
			foreach.addAttribute("collection", "list");
			foreach.addAttribute("item", "id");
			foreach.addAttribute("open", "(");
			foreach.addAttribute("close", ")");
			foreach.addAttribute("separator", ", ");
			foreach.addAttribute("index", "i");
			foreach.setText("#{id}");
			deleteByIds.add(foreach);
			// delete deleteByIds end

			//root
			//Element extRoot = DocumentHelper.createElement("mapper");
			root.remove(insertNode);
			// 输出不需要的节点
			removeNode(root);
			// 删除注释节点
			removeComment(root);
			removeComment(insertNode);
			
			// 插入节点
			List<Element> all = new ArrayList<>();
			root.elements().forEach(obj->{
				Element e = (Element) obj;
				all.add(e);
				if (e.getName().equals("delete")) {
					all.add(deleteByIds);
				}
			});
			all.add(insertNode);
			all.add(select);
			all.add(selectCount);
			all.add(sqlNode);
			
			root.elements().clear();
			all.forEach(e->root.add(e));

			document.setRootElement(root);
			// 可以使用 addDocType()方法添加文档类型说明。
			//extDoc.addDocType("mapper", "-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");

			OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
			//设置文件编码 
			xmlFormat.setEncoding("UTF-8");
			// 设置换行
			xmlFormat.setNewlines(true);
			xmlFormat.setNewLineAfterDeclaration(true);
			// 生成缩进
			xmlFormat.setIndent(true);
			// 删除空白
			// xmlFormat.setTrimText(true);
			// 使用4个空格进行缩进
			// xmlFormat.setIndentSize(4);

			XMLWriter fw = new XMLWriter(new FileWriter(xml), xmlFormat);
			LOGGER.debug(document.asXML());

			fw.write(document);
			fw.flush();
			fw.close();
			LOGGER.info("create mapper xml '{}' successful", xml.getPath());
		} catch (Exception e) { 
			LOGGER.error(xml.getAbsolutePath(), e);
		} 

	}

	public static String getXmlMappingPath() {

		ContextBean context = BootApplication.getContextBean();
		if (context==null) {
			return null;
		}

		String dir = context.getSqlMapGenerator().getTargetProject();
		String pkg = context.getSqlMapGenerator().getTargetPackage();
		File path = new File(dir, pkg.replace(".", "/"));

		return path.getAbsolutePath();
	}

	public static String getModelPath() {

		ContextBean context = BootApplication.getContextBean();
		if (context==null) {
			return null;
		}

		String dir = context.getJavaModelGenerator().getTargetProject();
		String pkg = context.getJavaModelGenerator().getTargetPackage();
		File path = new File(dir, pkg.replace(".", "/"));

		return path.getAbsolutePath();
	}

	public static String getMapperPath() {

		ContextBean context = BootApplication.getContextBean();
		if (context==null) {
			return null;
		}

		String dir = context.getJavaClientGenerator().getTargetProject();
		String pkg = context.getJavaClientGenerator().getTargetPackage();
		File path = new File(dir, pkg.replace(".", "/"));

		return path.getAbsolutePath();
	}

	public static String getModelParentPkg() {
		ContextBean context = BootApplication.getContextBean();
		String pkg = context.getJavaModelGenerator().getTargetPackage();
		pkg = pkg.substring(0, pkg.lastIndexOf("."));
		return pkg;
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
