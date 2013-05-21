package game;

import grid.Grid;
import grid.builder.FileGridBuilderDirector;
import grid.builder.RandomGridBuilderDirector;
import grid.builder.TronGridBuilder;
import gui.GUI;
import player.PlayerDataBase;
import ObjectronExceptions.builderExceptions.InvalidGridFileException;
import controllers.EndTurnController;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.NewGameController;
import controllers.PickUpItemController;
import controllers.UseItemController;

/**
 * The class that start the {@link GUI} and offers methods to start new
 * {@link Game games}. It contains the main method.
 * 
 */
public class GameRunner {
	
	private PlayerDataBase		playerDB;
	private Game				game;
	private GUIDataController	guiDataCont;
	private GUI					gui;
	
	/**
	 * main method, will create a new GameRunner instance to start the gui
	 * 
	 * @param args
	 *        arguments are ignored
	 */
	public static void main(String[] args) {
		new GameRunner();
	}
	
	/**
	 * Start the initialization and run the GUI.
	 */
	GameRunner() {
		this.playerDB = new PlayerDataBase();
		
		// create all the controllers, giving them the DB
		MoveController moveCont = new MoveController(this.playerDB);
		PickUpItemController pickUpCont = new PickUpItemController(this.playerDB);
		UseItemController useItemCont = new UseItemController(this.playerDB);
		EndTurnController endTurnCont = new EndTurnController(this.playerDB);
		NewGameController newGameCont = new NewGameController(this);
		// grid is still unknown
		this.guiDataCont = new GUIDataController(this.playerDB, null);
		
		gui = new GUI(moveCont, pickUpCont, useItemCont, newGameCont, endTurnCont, guiDataCont);
		
		// Set the initialized GUI as the gui for the controllers
		useItemCont.setGUI(gui);
		
		java.awt.EventQueue.invokeLater(gui);
	}
	
	/**
	 * Start a new {@link Game} with specified board dimensions and specified
	 * mode.
	 * 
	 * @param mode
	 *        the mode for the new game
	 * @param width
	 *        the height of the new board
	 * @param height
	 *        the width of the new board
	 * @throws IllegalArgumentException
	 *         The specified width and height must be greater then the minima.
	 * @throws IllegalStateException
	 *         The number of players as {@link GameMode#getNumberOfPlayers()
	 *         given by the mode} must be less then or equal to the number of
	 *         starting locations defined in grid to be created.
	 */
	public void newGame(GameMode mode, int width, int height) throws IllegalStateException {
		TronGridBuilder builder = new TronGridBuilder(mode.getEffectFactory());
		RandomGridBuilderDirector director = new RandomGridBuilderDirector(builder);
		director.setHeight(height);
		director.setWidth(width);
		director.construct();
		
		createGame(mode, builder.getResult());
	}
	
	/**
	 * Start a new game that is read from a file.
	 * 
	 * @param mode
	 *        The mode for the new game
	 * @param file
	 *        The file the grid is located in.
	 * @throws InvalidGridFileException
	 *         The grid file must adhere the correct rules and it cannot contain
	 *         invalid characters.
	 * @throws IllegalStateException
	 *         The number of players as {@link GameMode#getNumberOfPlayers()
	 *         given by the mode} must be less then or equal to the number of
	 *         starting locations defined in the grid to be created.
	 */
	public void newGame(GameMode mode, String file) throws InvalidGridFileException,
			IllegalStateException {
		TronGridBuilder builder = new TronGridBuilder(mode.getEffectFactory());
		FileGridBuilderDirector director = new FileGridBuilderDirector(builder, file);
		director.construct();
		
		createGame(mode, builder.getResult());
	}
	
	/**
	 * Creates a game with a specified mode and grid. This method will also set
	 * all references to the grid and fix the observers of the game.
	 * 
	 * @param mode
	 *        the mode for the game
	 * @param grid
	 *        the grid for the game
	 * @throws IllegalStateExcption
	 *         The number of players as {@link GameMode#getNumberOfPlayers()
	 *         given by the mode} must be less then or equal to the number of
	 *         starting locations defined in the grid.
	 */
	private void createGame(GameMode mode, Grid grid) throws IllegalStateException {
		guiDataCont.setGrid(grid);
		
		// fix observers of old game
		game.deleteObservers();
		
		// create a new Game and fix observers
		game = new Game(mode, grid, playerDB);
		game.addObserver(gui);
	}
	
}