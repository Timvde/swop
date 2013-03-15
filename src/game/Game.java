package game;

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
 * TODO
 * 
 * @author tom
 */
public class Game {
	
	private Grid				grid	= null;
	private PlayerDataBase		playerDB;
	private GUI					gui;
	private GUIDataController	guiDataCont;
	
	/**
	 * Start the program by creating a new Game.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	/**
	 * Start the initialisation and run the GUI.
	 */
	public void start() {
		this.playerDB = new PlayerDataBase(grid);  
		
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
	 * 
	 * @param width
	 * @param height
	 */
	public void newGame(int width, int height) {
		System.out.println("Creating new game with grid width " + width + " and height " + height);
		this.grid = new GridBuilder().setGridWidth(width).setGridHeigth(height).build();
		this.setGrid(this.grid);
		this.guiDataCont.setGrid(this.grid);
		this.gui.draw(this.grid);
		
		Coordinate[] playerStartingCoordinates = new Coordinate[] { new Coordinate(width - 1, 0),
				new Coordinate(0, height - 1) };
		playerDB.createNewDB(playerStartingCoordinates, grid);
	}
	
	/**
	 * 
	 * @param p
	 */
	public void endGame(Player p) {
		// TODO Auto-generated method stub
		
	}
}
