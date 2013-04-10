package game;

import java.util.List;
import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import gui.GUI;
import player.Player;
import player.PlayerDataBase;
import controllers.EndTurnController;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.NewGameController;
import controllers.PickUpItemController;
import controllers.UseItemController;

/**
 * Game controls and initializes a game
 * 
 * @author tom
 */
public class Game {
	
	private Grid				grid;
	private PlayerDataBase		playerDB;
	private GUI					gui;
	private GUIDataController	guiDataCont;
	
	/**
	 * main method
	 * 
	 * @param args
	 *        arguments are ignored
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	/**
	 * Start the initialisation and run the GUI.
	 */
	public void start() {
		this.playerDB = new PlayerDataBase();
		
		// create all the controllers, giving them the IPlayerDB
		MoveController moveCont = new MoveController(this.playerDB);
		PickUpItemController pickUpCont = new PickUpItemController(this.playerDB);
		UseItemController useItemCont = new UseItemController(this.playerDB);
		EndTurnController endTurnCont = new EndTurnController(this.playerDB);
		NewGameController newGameCont = new NewGameController(this);
		
		// Here grid is still null
		this.guiDataCont = new GUIDataController(this.playerDB, this.grid);
		
		this.gui = new GUI(moveCont, pickUpCont, useItemCont, newGameCont, endTurnCont,
				this.guiDataCont);
		
		// Set the initialized GUI as the gui for the controllers
		useItemCont.setGUI(gui);
		
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
		// TODO is this method used for testing purposes??
	}
	
	/**
	 * Start a new game with specified board dimensions
	 * 
	 * @param width
	 *        the height of the new board
	 * @param height
	 *        the width of the new board
	 */
	public void newGame(int width, int height) {
		System.out.println("Creating new game with grid width " + width + " and height " + height);
		List<Player> players = playerDB.createNewDB();
		this.grid = new GridBuilder(players).setGridWidth(width).setGridHeigth(height).build();
		this.setGrid(this.grid);
		this.guiDataCont.setGrid(this.grid);
		this.gui.draw(this.grid);
		
	}
	
	/**
	 * end the current game
	 * 
	 * @param p
	 *        the player who wins/loses the game
	 */
	public void endGame(Player p) {
		// TODO Auto-generated method stub
		
	}
}
