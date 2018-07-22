package org.mybatis.generator.base;

public class Const {
	
	public static String getRootPath(){
		
		return Const.class.getClassLoader().getResource("").getPath();
	}

}
