package org.mybatis.generator.add.ui.beans;

public class JavaModelGenerator extends BaseBean{

	private String targetPackage;
	private String targetProject;
	private String rootClass;
	private boolean enableSubPackages;
	private boolean trimStrings;
	
	public String getTargetPackage() {
		return targetPackage;
	}
	public String getTargetProject() {
		return targetProject;
	}
	public String getRootClass() {
		return rootClass;
	}
	public boolean isEnableSubPackages() {
		return enableSubPackages;
	}
	public boolean isTrimStrings() {
		return trimStrings;
	}
	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}
	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}
	public void setRootClass(String rootClass) {
		this.rootClass = rootClass;
	}
	public void setEnableSubPackages(boolean enableSubPackages) {
		this.enableSubPackages = enableSubPackages;
	}
	public void setTrimStrings(boolean trimStrings) {
		this.trimStrings = trimStrings;
	}
	
	
}
