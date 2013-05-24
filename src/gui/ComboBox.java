package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

public final class ComboBox {
	
	final JComboBox cb;
	Runnable clickHandler;
	
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