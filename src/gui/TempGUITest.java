package gui;

import game.Game;
import grid.Grid;
import java.util.*;
import controllers.GetInventoryListController;
import controllers.GetItemListController;
import controllers.MoveController;
import controllers.PickUpItemController;
import controllers.UseItemController;
import player.IPlayer;
import player.Player;

public class TempGUITest {
	
	public static void main(String[] args) {
		
		Game game = new Game();
		GUI gui = new GUI(new MoveController(game), new PickUpItemController(game),
				new UseItemController(game), new GetInventoryListController(game),
				new GetItemListController(game));
		java.awt.EventQueue.invokeLater(gui);
		ArrayList<IPlayer> players = new ArrayList<IPlayer>();
		IPlayer p1 = new Player(null);
		IPlayer p2 = new Player(null);
		System.out.println("p1: " + p1.getID() + " - p2: " + p2.getID());
		players.add(p1);
		players.add(p2);


		Grid grid = new Grid.Builder(game, players).setGridWidth(20).build();

		game.setGrid(grid);
		game.setCurrentPlayer(p1);
		gui.draw(grid);
	}
	
}
