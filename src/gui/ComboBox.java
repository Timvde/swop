package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

@SuppressWarnings("javadoc")
public final class ComboBox {
	
	@SuppressWarnings("rawtypes")
	final JComboBox	cb;
	Runnable		clickHandler;
	
	@SuppressWarnings("rawtypes")
	ComboBox(JComboBox cb) {
		this.cb = cb;
		cb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (clickHandler != null)
					clickHandler.run();
			}
			
		});
	}
	
	public void setClickHandler(Runnable clickHandler) {
		this.clickHandler = clickHandler;
	}
	
	public int getSelectedIndex() {
		return cb.getSelectedIndex();
	}
	
}
