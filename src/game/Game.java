package game;

import grid.FileDirector;
import grid.Grid;
import grid.RandomDirector;
import grid.builder.TronGridBuilder;
import gui.GUI;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import player.IPlayer;
import player.Player;
import player.PlayerDataBase;
import player.PlayerState;
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
		// make a new PlayerDB and observe it
		// (to be notified when a player has won/lost)
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
		// Grid must be notified of player switching to update the power
		// failures
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
		List<Player> players = playerDB.createNewDB();
		
		TronGridBuilder builder = new TronGridBuilder();
		RandomDirector director = new RandomDirector(builder);
		director.setHeight(height);
		director.setWidth(width);
		director.construct();
		
		grid = builder.getResult();
		this.setGrid(this.grid);
		this.guiDataCont.setGrid(this.grid);
		this.gui.draw(this.grid);
	}
	
	/**
	 * Start a new game that is read from a file.
	 * 
	 * @param file
	 *        The file the grid is located in.
	 */
	public void newGameFromFile(String file) {
		System.out.println("Creating new game from file: " + file);
		List<Player> players = playerDB.createNewDB();
		
		TronGridBuilder builder = new TronGridBuilder();
		FileDirector director = new FileDirector(builder);
		director.setFile(file);
		director.construct();
		
		grid = builder.getResult();
		this.setGrid(this.grid);
		this.guiDataCont.setGrid(this.grid);
		this.gui.draw(this.grid);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof PlayerDataBase && arg instanceof PlayerState) {
			this.handlePlayerDataBaseEvent((PlayerDataBase) o, (PlayerState) arg);
		}
		// else do nothing; return
	}
	
	/**
	 * This method will be called each time {@link PlayerDataBase} reports a
	 * Player-change (passing the {@link PlayerState} of the player whos turn is
	 * ended as an argument).
	 * 
	 * @param endedPlayerState
	 *        the {@link PlayerState} of the player whos turn is ended
	 * @param db
	 */
	private void handlePlayerDataBaseEvent(PlayerDataBase db, PlayerState endedPlayerState) {
		// only interested in finished or lost states; ignore active/waiting
		// states
		switch (endedPlayerState) {
			case FINISHED:
				this.endGameWithWinner(db.getCurrentPlayer());
				break;
			
			case LOST:
				this.endGameWithLoser(db.getCurrentPlayer());
		}
	}
	
	private void endGameWithLoser(IPlayer player) {
		// TODO Auto-generated method stub
		System.out.println("game is finished with loser " + player);
	}
	
	private void endGameWithWinner(IPlayer player) {
		// TODO Auto-generated method stub
		System.out.println("game is finished with winner " + player);
		
	}
}
