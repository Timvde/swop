package game;

import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import gui.GUI;
import java.util.Observable;
import java.util.Observer;
import player.PlayerDataBase;
import square.ASquare;
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
public class Game implements Observer {
	
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
	 * Start the initialization and run the GUI.
	 */
	public void start() {
		// make a new PlayerDB and observe it (to be notified when a player has
		// won/lost)
		this.playerDB = new PlayerDataBase();
		playerDB.addObserver(this);
		
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
		//Grid must be notified of player switching to update the power failures
		this.playerDB.addObserver(grid);
		
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
		this.grid = new GridBuilder().setGridWidth(width).setGridHeigth(height).build();
		this.setGrid(this.grid);
		this.guiDataCont.setGrid(this.grid);
		this.gui.draw(this.grid);
		
		ASquare[] playerStartingCoordinates = new ASquare[] {
				grid.getGrid().get(new Coordinate(width - 1, 0)),
				grid.getGrid().get(new Coordinate(0, height - 1)) };
		playerDB.createNewDB(playerStartingCoordinates);
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
