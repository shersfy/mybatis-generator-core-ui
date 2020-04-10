package org.shersfy.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加载配置
 * @author Young
 * @date 2020-04-08
 */
public class LocalConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalConfig.class);
	private static final String CONF_PATH = "conf.properties";
	
	private static String appName;
	private static String appVersion;
	private static String outputPath;
	private static String outputConf;
	private static String pkgXml;
	private static String pkgMappger;
	private static String pkgModel;
	private static String pkgRootClass;
	private static String jdbcSupport;
	private static String jdbcDriver;
	private static String jdbcUrl;
	private static String jdbcUsername;
	private static String jdbcPassword;

	static {
		LOGGER.info("load configuration properties {} start ...", CONF_PATH);
		InputStream inputStream = ClassLoader.getSystemResourceAsStream(CONF_PATH);
		Properties properties   = new Properties();
		try {
			properties.load(inputStream);
			appName    = properties.getProperty("application.name");
			appVersion = properties.getProperty("application.version");
			outputPath = properties.getProperty("output.path");
			outputConf = properties.getProperty("output.conf");
			pkgXml 	   = properties.getProperty("output.pkg.xml");
			pkgMappger = properties.getProperty("output.pkg.mapper");
			pkgModel	 = properties.getProperty("output.pkg.model");
			pkgRootClass = properties.getProperty("output.pkg.root.class");
			
			jdbcSupport	= properties.getProperty("jdbc.support");
			jdbcDriver	= properties.getProperty("jdbc.driver");
			jdbcUrl		= properties.getProperty("jdbc.url");
			jdbcUsername= properties.getProperty("jdbc.username");
			jdbcPassword= properties.getProperty("jdbc.password");
		
			//print
			properties.forEach((n,v)->LOGGER.info(" {}={}", n, v));
		} catch (Exception ex) {
			LOGGER.error("", ex);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		LOGGER.info("load configuration properties {} successful", CONF_PATH);
	}
	
	public static File getBasedir() {
		URL url = ClassLoader.getSystemResource(CONF_PATH);
		File conf = new File(url.getPath());
		return conf.getParentFile().getParentFile();
	}

	public static String getAppName() {
		return appName;
	}

	public static String getAppVersion() {
		return appVersion;
	}

	public static String getOutputPath() {
		return outputPath;
	}

	public static String getOutputConf() {
		return outputConf;
	}

	public static String getPkgXml() {
		return pkgXml;
	}

	public static String getPkgMappger() {
		return pkgMappger;
	}

	public static String getPkgModel() {
		return pkgModel;
	}

	public static String getPkgRootClass() {
		return pkgRootClass;
	}
	
	public static String getJdbcSupport() {
		return jdbcSupport;
	}

	public static String getJdbcDriver() {
		return jdbcDriver;
	}

	public static String getJdbcUrl() {
		return jdbcUrl;
	}

	public static String getJdbcUsername() {
		return jdbcUsername;
	}

	public static String getJdbcPassword() {
		return jdbcPassword;
	}

}
