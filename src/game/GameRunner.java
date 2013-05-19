package game;

import grid.Grid;
import grid.builder.FileGridBuilderDirector;
import grid.builder.RandomGridBuilderDirector;
import grid.builder.TronGridBuilder;
import gui.GUI;
import java.util.Iterator;
import player.PlayerDataBase;
import square.SquareContainer;
import ObjectronExceptions.builderExceptions.InvalidGridFileException;
import controllers.EndTurnController;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.NewGameController;
import controllers.PickUpItemController;
import controllers.UseItemController;

/**
 * The class that start the {@link GUI} and offers methods to start new
 * {@link Game games}. This class contains the main method.
 * 
 */
public class GameRunner {
	
	private PlayerDataBase		playerDB;
	private Game				game;
	private GUIDataController	guiDataCont;
	
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
		// grid is still unknown
		this.guiDataCont = new GUIDataController(this.playerDB, null);
		NewGameController newGameCont = new NewGameController(this);
		
		GUI gui = new GUI(moveCont, pickUpCont, useItemCont, newGameCont, endTurnCont, guiDataCont);
		
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
	 *         starting locations defined in the created grid.
	 */
	public void newGame(GameMode mode, int width, int height) throws IllegalArgumentException,
			IllegalStateException {
		TronGridBuilder builder = new TronGridBuilder();
		RandomGridBuilderDirector director = new RandomGridBuilderDirector(builder);
		director.setHeight(height);
		director.setWidth(width);
		director.construct();
		
		createNewGame(mode, builder.getResult());
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
	 *         starting locations defined in the created grid.
	 */
	public void newGame(GameMode mode, String file) throws InvalidGridFileException {
		TronGridBuilder builder = new TronGridBuilder();
		FileGridBuilderDirector director = new FileGridBuilderDirector(builder, file);
		director.construct();
		
		createNewGame(mode, builder.getResult());
	}
	
	/**
	 * Creates a new Game with a specified mode and grid.
	 * 
	 * @throws IllegalStateException
	 *         The number of players as {@link GameMode#getNumberOfPlayers()
	 *         given by the mode} must be less then or equal to the number of
	 *         starting locations defined in the created grid.
	 */
	private void createNewGame(GameMode mode, Grid grid) throws IllegalStateException {
		if (grid.getAllStartingPositions().size() < mode.getNumberOfPlayers())
			throw new IllegalArgumentException(
					" The number of players must be less then or equal to the number of starting locations defined in the grid");
		
		// unwrap the superfluous playerstarts
		for (int i = mode.getNumberOfPlayers() - 1; i < grid.getAllStartingPositions().size(); i++) {
			// ... TODO
			// getAllStartingPositions moet de juiste volgorde teruggeven een
			// lijst om te removen
		}
		
		// make all the squares in the new grid observer of the db
		Iterator<SquareContainer> iterator = grid.getGridIterator();
		while (iterator.hasNext()) {
			SquareContainer square = (SquareContainer) iterator.next();
			this.playerDB.addObserver(square);
		}
		
		this.guiDataCont.setGrid(grid);
		
		playerDB.createNewDB(grid.getAllStartingPositions());
		
		playerDB.deleteObserver(this.game);
		this.game = new Game(mode);
		playerDB.addObserver(game);
	}
	
}
