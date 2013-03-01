package gui;

import grid.Grid;
import java.util.*;
import player.IPlayer;
import player.Player;


public class TempGUITest {
	
	public static void main(String[] args) {
		GUI gui = new GUI(null, null, null, null, null);
		java.awt.EventQueue.invokeLater(gui);
		ArrayList<IPlayer> players = new ArrayList<IPlayer>();
		IPlayer p1 = new Player(null);
		IPlayer p2 = new Player(null);
		System.out.println("p1: " + p1.getID() + " - p2: " + p2.getID());
		players.add(p1);
		players.add(p2);

		Grid grid = new Grid.Builder(players).build();
		gui.draw(grid);
	}
	
}
