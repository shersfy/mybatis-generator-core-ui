package org.mybatis.generator.add.ui;

import javax.swing.JButton;

public class Button extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	public Button(){
		
	}
	
	public Button(String text){
		super(text);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
