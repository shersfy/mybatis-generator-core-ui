package org.mybatis.generator.test;

import java.net.URL;

import org.junit.Test;

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

}
