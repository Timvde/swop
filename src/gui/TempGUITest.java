package gui;

import grid.Grid;


public class TempGUITest {
	
	public static void main(String[] args) {
		GUI gui = new GUI(null, null, null, null, null);
		java.awt.EventQueue.invokeLater(gui);

		Grid grid = new Grid.Builder().build();
		gui.draw(grid);
	}
	
}
