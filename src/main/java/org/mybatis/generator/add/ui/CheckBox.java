package org.mybatis.generator.add.ui;

import javax.swing.JCheckBox;

public class CheckBox extends JCheckBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	public CheckBox(){
		
	}
	
	public CheckBox(String text){
		super(text);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
