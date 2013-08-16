package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("javadoc")
public abstract class AGUI {
	
	private JFrame frame;
	private JPanel panel;
	
	@SuppressWarnings("serial")
	public AGUI(String title, final int width, final int height) {
		frame = new JFrame(title);
		panel = new JPanel(null) {
			@Override public void paintComponent(Graphics g) {
				super.paintComponent(g);
				AGUI.this.paint((Graphics2D)g);
			}
			
			public Dimension getPreferredSize() {
				return new Dimension(width, height);
			}
		};
		panel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouseClick(e.getX(), e.getY(), e.getClickCount() == 2);
			}
			
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public JPanel getPanel() {
		return this.panel;
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public abstract void paint(Graphics2D graphics);
	
	public void handleMouseClick(int x, int y, boolean doubleClick) {
	}
	
	public final void repaint() {
		panel.repaint();
	}
	
	public final Button createButton(int x, int y, int width, int height, Runnable clickHandler) {
		JButton button = new JButton();
		button.setLocation(x, y);
		button.setSize(width, height);
		panel.add(button);
		Button b = new Button(button);
		b.setClickHandler(clickHandler);
		return b;
	}
	
	@SuppressWarnings("rawtypes")
	public final List createList(int x, int y, int width, int height, Runnable clickHandler) {
		JList list = new JList();
		list.setLocation(x, y);
		list.setSize(width, height);
		panel.add(list);
		List l = new List(list);
		l.setClickHandler(clickHandler);
		
		return l;
	}
	
	@SuppressWarnings("rawtypes")
	public final ComboBox createComboBox(int x, int y, int w, int h, String[] options, Runnable clickHandler) {
		@SuppressWarnings("unchecked")
		JComboBox cb = new JComboBox(options);
		cb.setLocation(x, y);
		cb.setSize(w, h);
		panel.add(cb);
		ComboBox c = new ComboBox(cb);
		c.setClickHandler(clickHandler);
		return c;
	}
	
	public final TextField createTextField(int x, int y, int width, int height) {
		JTextField textfield = new JTextField();
		textfield.setLocation(x, y);
		textfield.setSize(width, height);
		panel.add(textfield);
		TextField t = new TextField(textfield);
		return t;
	}
	
	public final Image loadImage(String url, int width, int height) {
		Image image = panel.getToolkit().createImage(ClassLoader.getSystemResource(url));
		MediaTracker tracker = new MediaTracker(panel);
		tracker.addImage(image, 1, width, height);
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return image;
	}
	
}