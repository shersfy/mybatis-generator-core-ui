package org.mybatis.generator.add.ui.beans;

public class JavaTypeResolver extends BaseBean{

	private boolean forceBigDecimals;
	
	public JavaTypeResolver(boolean forceBigDecimals){
		this.forceBigDecimals = forceBigDecimals;
	}

	public boolean isForceBigDecimals() {
		return forceBigDecimals;
	}

	public void setForceBigDecimals(boolean forceBigDecimals) {
		this.forceBigDecimals = forceBigDecimals;
	}
	
	
}
