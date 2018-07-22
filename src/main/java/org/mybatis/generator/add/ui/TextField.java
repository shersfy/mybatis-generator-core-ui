package org.mybatis.generator.add.ui;

import javax.swing.JTextField;

public class TextField extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	public TextField(){}
	
	public TextField(String text){
		super(text);
	}
	
	public TextField(int columns){
		super(columns);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
