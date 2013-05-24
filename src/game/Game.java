package game;

import grid.Grid;
import grid.GuiGridAdapter;
import grid.builder.FileGridBuilderDirector;
import grid.builder.RandomGridBuilderDirector;
import grid.builder.TronGridBuilder;
import gui.GUI;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import player.Player;
import player.PlayerDataBase;
import player.PlayerState;
import player.TurnEvent;
import square.AbstractSquare;
import ObjectronExceptions.builderExceptions.InvalidGridFileException;
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
		new Game();
	}
	
	/**
	 * Start the initialization and run the GUI.
	 */
	public Game() {
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
		this.guiDataCont = new GUIDataController(this.playerDB, null);
		
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
		if (grid == null)
			throw new IllegalArgumentException("grid cannot be null");
		this.grid = grid;
		
		for (AbstractSquare square : grid.getGrid().values())
			this.playerDB.addObserver(square);
	}
	
	/**
	 * Start a new game with specified board dimensions
	 * 
	 * @param width
	 *        the height of the new board
	 * @param height
	 *        the width of the new board
	 * @throws IllegalArgumentException
	 *         The specified width and height must be greater then the minima.
	 */
	public void newGame(int width, int height) throws IllegalArgumentException {
		System.out.println("Creating new game with grid width " + width + " and height " + height);
		
		TronGridBuilder builder = new TronGridBuilder();
		RandomGridBuilderDirector director = new RandomGridBuilderDirector(builder);
		director.setHeight(height);
		director.setWidth(width);
		director.construct();
		
		startGameWithGrid(builder.getResult());
	}
	
	/**
	 * Start a new game that is read from a file.
	 * 
	 * @param file
	 *        The file the grid is located in.
	 * @throws InvalidGridFileException
	 *         The grid file must adhere the correct rules and it cannot contain
	 *         invalid characters.
	 */
	public void newGameFromFile(String file) throws InvalidGridFileException {
		System.out.println("Creating new game from file: " + file);
		
		TronGridBuilder builder = new TronGridBuilder();
		FileGridBuilderDirector director;
		try {
			director = new FileGridBuilderDirector(builder, file);
		}
		catch (FileNotFoundException e) {
			throw new InvalidGridFileException("");
		}
		director.construct();
		
		startGameWithGrid(builder.getResult());
	}
	
	private void startGameWithGrid(Grid grid) {
		this.setGrid(grid);
		this.guiDataCont.setGrid(new GuiGridAdapter(grid));
		this.gui.draw();
		
		
		
		this.playerDB.createNewDB(grid.getAllStartingPositions());
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof PlayerDataBase && arg == TurnEvent.END_GAME) {
			this.handleEndGameEvent((PlayerDataBase) o);
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
	private void handleEndGameEvent(PlayerDataBase db) {
		switch (db.getCurrentPlayer().getPlayerState()) {
			case FINISHED:
				this.endGameWithWinner(db.getCurrentPlayer());
				break;
			
			case LOST:
				this.endGameWithLoser(db.getCurrentPlayer());
				break;
			default:
				// only interested in finished or lost states; ignore
				// active/waiting
				// states
				break;
		}
	}
	
	private void endGameWithLoser(Player player) {
		// TODO Auto-generated method stub
		System.out.println("game is finished with loser " + player);
	}
	
	private void endGameWithWinner(Player player) {
		// TODO Auto-generated method stub
		System.out.println("game is finished with winner " + player);
		
	}
}
