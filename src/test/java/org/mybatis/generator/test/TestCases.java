package org.mybatis.generator.test;

import java.net.URL;
import java.text.MessageFormat;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;
import org.shersfy.model.BaseEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestCases {
	
	@Test
	public void test01() {
		// ../target/test-classes/
	    URL resource = TestCases.class.getResource("/");
	    System.out.println(resource);

        // ../target/test-classes/org/mybatis/generator/test/
	    resource = TestCases.class.getResource("");
	    System.out.println(resource);
	    
	    // ../target/test-classes/
	    resource = TestCases.class.getClassLoader().getResource("");
	    System.out.println(resource);
	    
	    // null
	    resource = TestCases.class.getClassLoader().getResource("/");
	    System.out.println(resource);
	}

	
	@Test
	public void test02() {
		String text1 = "{\"java.runtime.name\":\"Java(TM) SE Runtime Environment\",\"sun.boot.library.path\":\"C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\bin\",\"java.vm.version\":\"25.181-b13\",\"java.vm.vendor\":\"Oracle Corporation\",\"java.vendor.url\":\"http://java.oracle.com/\",\"path.separator\":\";\",\"java.vm.name\":\"Java HotSpot(TM) 64-Bit Server VM\",\"file.encoding.pkg\":\"sun.io\",\"user.country\":\"CN\",\"user.script\":\"\",\"sun.java.launcher\":\"SUN_STANDARD\",\"sun.os.patch.level\":\"\",\"java.vm.specification.name\":\"Java Virtual Machine Specification\",\"user.dir\":\"D:\\\\runner\\\\mybatis-ui-v1.3.2\",\"java.runtime.version\":\"1.8.0_181-b13\",\"java.awt.graphicsenv\":\"sun.awt.Win32GraphicsEnvironment\",\"java.endorsed.dirs\":\"C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\endorsed\",\"os.arch\":\"amd64\",\"java.io.tmpdir\":\"C:\\\\Users\\\\shers\\\\AppData\\\\Local\\\\Temp\\\\\",\"line.separator\":\"\\r\\n\",\"java.vm.specification.vendor\":\"Oracle Corporation\",\"user.variant\":\"\",\"os.name\":\"Windows 10\",\"sun.jnu.encoding\":\"UTF-8\",\"java.library.path\":\"C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\bin;C:\\\\WINDOWS\\\\Sun\\\\Java\\\\bin;C:\\\\WINDOWS\\\\system32;C:\\\\WINDOWS;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\bin;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\bin;C:\\\\Program Files (x86)\\\\Common Files\\\\Oracle\\\\Java\\\\javapath;C:\\\\Program Files (x86)\\\\Intel\\\\Intel(R) Management Engine Components\\\\iCLS\\\\;C:\\\\Program Files\\\\Intel\\\\Intel(R) Management Engine Components\\\\iCLS\\\\;C:\\\\WINDOWS\\\\system32;C:\\\\WINDOWS;C:\\\\WINDOWS\\\\System32\\\\Wbem;C:\\\\WINDOWS\\\\System32\\\\WindowsPowerShell\\\\v1.0\\\\;C:\\\\WINDOWS\\\\System32\\\\OpenSSH\\\\;C:\\\\Program Files (x86)\\\\Intel\\\\Intel(R) Management Engine Components\\\\DAL;C:\\\\Program Files\\\\Intel\\\\Intel(R) Management Engine Components\\\\DAL;C:\\\\Program Files (x86)\\\\Intel\\\\Intel(R) Management Engine Components\\\\IPT;C:\\\\Program Files\\\\Intel\\\\Intel(R) Management Engine Components\\\\IPT;C:\\\\Program Files\\\\Git\\\\cmd;C:\\\\Program Files\\\\nodejs\\\\;D:\\\\apache\\\\apache-maven-3.5.4\\\\bin;C:\\\\Go\\\\bin;D:\\\\apache\\\\gradle-6.2.2\\\\bin;C:\\\\Users\\\\shers\\\\AppData\\\\Local\\\\Microsoft\\\\WindowsApps;C:\\\\Users\\\\shers\\\\AppData\\\\Roaming\\\\npm;C:\\\\Users\\\\shers\\\\go\\\\bin;;.\",\"java.specification.name\":\"Java Platform API Specification\",\"java.class.version\":\"52.0\",\"sun.management.compiler\":\"HotSpot 64-Bit Tiered Compilers\",\"os.version\":\"10.0\",\"user.home\":\"C:\\\\Users\\\\shers\",\"user.timezone\":\"Asia/Shanghai\",\"java.awt.printerjob\":\"sun.awt.windows.WPrinterJob\",\"file.encoding\":\"UTF-8\",\"java.specification.version\":\"1.8\",\"java.class.path\":\"conf;lib/ant-1.8.2.jar;lib/ant-launcher-1.8.2.jar;lib/commons-io-2.4.jar;lib/commons-lang-2.6.jar;lib/dom4j-1.6.1.jar;lib/fastjson-1.2.4.jar;lib/log4j-1.2.17.jar;lib/mybatis-generator-core-ui-1.3.2.jar;lib/mysql-connector-java-5.1.40.jar;lib/slf4j-api-1.7.25.jar;lib/slf4j-log4j12-1.7.21.jar;lib/xml-apis-1.0.b2.jar;./\",\"user.name\":\"shers\",\"java.vm.specification.version\":\"1.8\",\"sun.java.command\":\"org.mybatis.app.boot.BootApplication\",\"java.home\":\"C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\",\"sun.arch.data.model\":\"64\",\"user.language\":\"zh\",\"java.specification.vendor\":\"Oracle Corporation\",\"awt.toolkit\":\"sun.awt.windows.WToolkit\",\"java.vm.info\":\"mixed mode\",\"java.version\":\"1.8.0_181\",\"java.ext.dirs\":\"C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\ext;C:\\\\WINDOWS\\\\Sun\\\\Java\\\\lib\\\\ext\",\"sun.boot.class.path\":\"C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\resources.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\rt.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\sunrsasign.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\jsse.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\jce.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\charsets.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\jfr.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\classes\",\"java.vendor\":\"Oracle Corporation\",\"sun.stderr.encoding\":\"ms936\",\"file.separator\":\"\\\\\",\"java.vendor.url.bug\":\"http://bugreport.sun.com/bugreport/\",\"sun.io.unicode.encoding\":\"UnicodeLittle\",\"sun.cpu.endian\":\"little\",\"sun.stdout.encoding\":\"ms936\",\"sun.desktop\":\"windows\",\"sun.cpu.isalist\":\"amd64\"}";
		String text2 = "{\"java.runtime.name\":\"Java(TM) SE Runtime Environment\",\"sun.boot.library.path\":\"C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\bin\",\"java.vm.version\":\"25.181-b13\",\"java.vm.vendor\":\"Oracle Corporation\",\"java.vendor.url\":\"http://java.oracle.com/\",\"path.separator\":\";\",\"java.vm.name\":\"Java HotSpot(TM) 64-Bit Server VM\",\"file.encoding.pkg\":\"sun.io\",\"user.country\":\"CN\",\"user.script\":\"\",\"sun.java.launcher\":\"SUN_STANDARD\",\"sun.os.patch.level\":\"\",\"java.vm.specification.name\":\"Java Virtual Machine Specification\",\"user.dir\":\"D:\\\\workspace-sts\\\\mybatis-generator-core-ui\",\"java.runtime.version\":\"1.8.0_181-b13\",\"java.awt.graphicsenv\":\"sun.awt.Win32GraphicsEnvironment\",\"java.endorsed.dirs\":\"C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\endorsed\",\"os.arch\":\"amd64\",\"java.io.tmpdir\":\"C:\\\\Users\\\\shers\\\\AppData\\\\Local\\\\Temp\\\\\",\"line.separator\":\"\\r\\n\",\"java.vm.specification.vendor\":\"Oracle Corporation\",\"user.variant\":\"\",\"os.name\":\"Windows 10\",\"sun.jnu.encoding\":\"GBK\",\"java.library.path\":\"C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\bin;C:\\\\WINDOWS\\\\Sun\\\\Java\\\\bin;C:\\\\WINDOWS\\\\system32;C:\\\\WINDOWS;C:/Program Files/Java/jdk1.8.0_181/bin/../jre/bin/server;C:/Program Files/Java/jdk1.8.0_181/bin/../jre/bin;C:/Program Files/Java/jdk1.8.0_181/bin/../jre/lib/amd64;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\bin;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\bin;C:\\\\Program Files (x86)\\\\Common Files\\\\Oracle\\\\Java\\\\javapath;C:\\\\Program Files (x86)\\\\Intel\\\\Intel(R) Management Engine Components\\\\iCLS\\\\;C:\\\\Program Files\\\\Intel\\\\Intel(R) Management Engine Components\\\\iCLS\\\\;C:\\\\WINDOWS\\\\system32;C:\\\\WINDOWS;C:\\\\WINDOWS\\\\System32\\\\Wbem;C:\\\\WINDOWS\\\\System32\\\\WindowsPowerShell\\\\v1.0\\\\;C:\\\\WINDOWS\\\\System32\\\\OpenSSH\\\\;C:\\\\Program Files (x86)\\\\Intel\\\\Intel(R) Management Engine Components\\\\DAL;C:\\\\Program Files\\\\Intel\\\\Intel(R) Management Engine Components\\\\DAL;C:\\\\Program Files (x86)\\\\Intel\\\\Intel(R) Management Engine Components\\\\IPT;C:\\\\Program Files\\\\Intel\\\\Intel(R) Management Engine Components\\\\IPT;C:\\\\Program Files\\\\Git\\\\cmd;C:\\\\Program Files\\\\nodejs\\\\;D:\\\\apache\\\\apache-maven-3.5.4\\\\bin;C:\\\\Go\\\\bin;D:\\\\apache\\\\gradle-6.2.2\\\\bin;C:\\\\Users\\\\shers\\\\AppData\\\\Local\\\\Microsoft\\\\WindowsApps;C:\\\\Users\\\\shers\\\\AppData\\\\Roaming\\\\npm;C:\\\\Users\\\\shers\\\\go\\\\bin;;D:\\\\sts-4.3.0;;.\",\"java.specification.name\":\"Java Platform API Specification\",\"java.class.version\":\"52.0\",\"sun.management.compiler\":\"HotSpot 64-Bit Tiered Compilers\",\"os.version\":\"10.0\",\"user.home\":\"C:\\\\Users\\\\shers\",\"user.timezone\":\"Asia/Shanghai\",\"java.awt.printerjob\":\"sun.awt.windows.WPrinterJob\",\"file.encoding\":\"UTF-8\",\"java.specification.version\":\"1.8\",\"java.class.path\":\"D:\\\\workspace-sts\\\\mybatis-generator-core-ui\\\\target\\\\classes;C:\\\\Users\\\\shers\\\\.m2\\\\repository\\\\org\\\\apache\\\\ant\\\\ant\\\\1.8.2\\\\ant-1.8.2.jar;C:\\\\Users\\\\shers\\\\.m2\\\\repository\\\\org\\\\apache\\\\ant\\\\ant-launcher\\\\1.8.2\\\\ant-launcher-1.8.2.jar;C:\\\\Users\\\\shers\\\\.m2\\\\repository\\\\commons-lang\\\\commons-lang\\\\2.6\\\\commons-lang-2.6.jar;C:\\\\Users\\\\shers\\\\.m2\\\\repository\\\\com\\\\alibaba\\\\fastjson\\\\1.2.4\\\\fastjson-1.2.4.jar;C:\\\\Users\\\\shers\\\\.m2\\\\repository\\\\org\\\\slf4j\\\\slf4j-api\\\\1.7.25\\\\slf4j-api-1.7.25.jar;C:\\\\Users\\\\shers\\\\.m2\\\\repository\\\\org\\\\slf4j\\\\slf4j-log4j12\\\\1.7.21\\\\slf4j-log4j12-1.7.21.jar;C:\\\\Users\\\\shers\\\\.m2\\\\repository\\\\log4j\\\\log4j\\\\1.2.17\\\\log4j-1.2.17.jar;C:\\\\Users\\\\shers\\\\.m2\\\\repository\\\\commons-io\\\\commons-io\\\\2.4\\\\commons-io-2.4.jar;C:\\\\Users\\\\shers\\\\.m2\\\\repository\\\\dom4j\\\\dom4j\\\\1.6.1\\\\dom4j-1.6.1.jar;C:\\\\Users\\\\shers\\\\.m2\\\\repository\\\\xml-apis\\\\xml-apis\\\\1.0.b2\\\\xml-apis-1.0.b2.jar;D:\\\\workspace-sts\\\\mybatis-generator-core-ui\\\\lib\\\\ojdbc6.jar;D:\\\\workspace-sts\\\\mybatis-generator-core-ui\\\\lib\\\\sqljdbc4.jar;C:\\\\Users\\\\shers\\\\.m2\\\\repository\\\\mysql\\\\mysql-connector-java\\\\5.1.40\\\\mysql-connector-java-5.1.40.jar;D:\\\\sts-4.3.0\\\\configuration\\\\org.eclipse.osgi\\\\244\\\\0\\\\.cp\\\\lib\\\\javaagent-shaded.jar\",\"user.name\":\"shers\",\"java.vm.specification.version\":\"1.8\",\"sun.java.command\":\"org.mybatis.app.boot.BootApplication\",\"java.home\":\"C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\",\"sun.arch.data.model\":\"64\",\"user.language\":\"zh\",\"java.specification.vendor\":\"Oracle Corporation\",\"awt.toolkit\":\"sun.awt.windows.WToolkit\",\"java.vm.info\":\"mixed mode\",\"java.version\":\"1.8.0_181\",\"java.ext.dirs\":\"C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\ext;C:\\\\WINDOWS\\\\Sun\\\\Java\\\\lib\\\\ext\",\"sun.boot.class.path\":\"C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\resources.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\rt.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\sunrsasign.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\jsse.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\jce.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\charsets.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\lib\\\\jfr.jar;C:\\\\Program Files\\\\Java\\\\jdk1.8.0_181\\\\jre\\\\classes\",\"java.vendor\":\"Oracle Corporation\",\"file.separator\":\"\\\\\",\"java.vendor.url.bug\":\"http://bugreport.sun.com/bugreport/\",\"sun.io.unicode.encoding\":\"UnicodeLittle\",\"sun.cpu.endian\":\"little\",\"sun.desktop\":\"windows\",\"sun.cpu.isalist\":\"amd64\"}";
		
		JSONObject json1 = JSON.parseObject(text1);
		JSONObject json2 = JSON.parseObject(text2);
		
		json1.keySet().forEach(key->{
			if (!json1.getString(key).equals(json2.getString(key))) {
				System.out.println(String.format("BAT \t%s=%s", key, json1.getString(key)));
				System.out.println(String.format("STS \t%s=%s", key, json2.getString(key)));
				System.out.println("===========");
			}
		});
		
	}
	
	@Test
	public void test03() {
		
		String msg = "我的名字是{0}, 我{1}岁 {s}";
		msg = MessageFormat.format(msg, "张三", 20);
		System.out.println(msg);
	}
	
	@Test
	public void test04() {
		System.out.println(ClassUtils.getShortClassName(BaseEntity.class.getName()));
	}
}
