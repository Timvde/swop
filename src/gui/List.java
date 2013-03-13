package gui;

import java.util.Vector;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("rawtypes")
public final class List {
	
	final JList list;
	Runnable clickHandler;
	
	List(JList list) {
		this.list = list;
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (clickHandler != null)
					clickHandler.run();
			}
			
		});
	}
	
	public Object getSelectedValue() {
		return this.list.getSelectedValue();
	}
	
	@SuppressWarnings("unchecked")
	public void setListData(Vector v) {
		this.list.setListData(v);
	}
	
	public void setClickHandler(Runnable clickHandler) {
		this.clickHandler = clickHandler;
	}

}