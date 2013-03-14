package game;

import grid.Grid;
import grid.GridBuilder;
import gui.GUI;
import java.util.ArrayList;
import java.util.List;
import player.IPlayer;
import player.Player;
import player.PlayerDataBase;
import player.PlayerDatabase;
import controllers.EndTurnController;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.NewGameController;
import controllers.PickUpItemController;
import controllers.UseItemController;

/**
 * TODO
 * 
 * @author tom
 */
public class Game {
	
	private Grid				grid	= null;
	private PlayerDataBase		playerDB;
	private GUI					gui;
	private GUIDataController	guiDataCont;
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void start() {
		// TODO initialise stuff
		
		// TODO playerDB contstr?
		this.playerDB = new PlayerDataBase();
		
		MoveController moveCont = new MoveController(this.playerDB);
		PickUpItemController pickUpCont = new PickUpItemController(this.playerDB);
		UseItemController useItemCont = new UseItemController(this.playerDB);
		NewGameController newGameCont = new NewGameController(this);
		EndTurnController endTurnCont = new EndTurnController(this.playerDB);
		this.guiDataCont = new GUIDataController(this.playerDB, this.grid);
		
		this.gui = new GUI(moveCont, pickUpCont, useItemCont, newGameCont, endTurnCont,
				this.guiDataCont);
		
		java.awt.EventQueue.invokeLater(gui);
	}
	
	/**
	 * Set the grid of the game.
	 * 
	 * @param grid
	 *        The grid to set.
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	/**
	 * 
	 * @param width
	 * @param height
	 */
	public void newGame(int width, int height) {

		List<IPlayer> players = playerDB.createNewDB();
		
		
		System.out.println("Creating new game with grid width " + width + " and height " + height);
		this.grid = new GridBuilder(players).setGridWidth(width).setGridHeigth(height).build();
		
		setGrid(this.grid);
		

		for (IPlayer player : players) 
			player.setGrid(grid);
		
		this.guiDataCont.setGrid(this.grid);
		this.gui.draw(this.grid);
	}
	
	/**
	 * 
	 * @param p
	 */
	public void endGame(Player p) {
		// TODO Auto-generated method stub
		
	}
}
