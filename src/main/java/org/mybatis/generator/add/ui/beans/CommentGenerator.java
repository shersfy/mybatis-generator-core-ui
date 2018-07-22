package org.mybatis.generator.add.ui.beans;


public class CommentGenerator extends BaseBean{
	// 是否生成注释
	private boolean suppressAllComments;
	// 注释是否追加时间(注释开启时有效)
	private boolean suppressDate;
	
	public boolean isSuppressAllComments() {
		return suppressAllComments;
	}

	public boolean isSuppressDate() {
		return suppressDate;
	}

	public void setSuppressAllComments(boolean suppressAllComments) {
		this.suppressAllComments = suppressAllComments;
	}

	public void setSuppressDate(boolean suppressDate) {
		this.suppressDate = suppressDate;
	}

	
}
