package gui;

import javax.swing.JTextField;

@SuppressWarnings("javadoc")
public final class TextField {
	
	final JTextField	textfield;
	Runnable			clickHandler;
	
	TextField(JTextField textfield) {
		this.textfield = textfield;
	}
	
	public String getText() {
		return this.textfield.getText();
	}
	
	public void setText(String t) {
		this.textfield.setText(t);
	}
	
	public void enable() {
		this.textfield.setEnabled(true);
	}
	
	public void disable() {
		this.textfield.setEnabled(false);
	}
}
