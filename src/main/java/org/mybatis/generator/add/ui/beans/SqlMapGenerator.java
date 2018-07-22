package org.mybatis.generator.add.ui.beans;

public class SqlMapGenerator extends BaseBean {

	private String targetPackage;
	private String targetProject;
	private boolean enableSubPackages;
	
	public String getTargetPackage() {
		return targetPackage;
	}
	public String getTargetProject() {
		return targetProject;
	}
	public boolean isEnableSubPackages() {
		return enableSubPackages;
	}
	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}
	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}
	public void setEnableSubPackages(boolean enableSubPackages) {
		this.enableSubPackages = enableSubPackages;
	}
	
	

}
