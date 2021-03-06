package controllers;

import game.CTFMode;
import game.GameMode;
import game.GameRunner;
import game.RaceMode;
import java.io.FileNotFoundException;
import ObjectronExceptions.builderExceptions.GridBuildException;
import ObjectronExceptions.builderExceptions.InvalidGridFileException;

/**
 * A controller for the GUI to create new games.
 * 
 */
public class NewGameController {
	
	private GameRunner	gameRunner;
	
	/**
	 * Create a new game controller with a given game.
	 * 
	 * @param gameRunner
	 *        The game reference.
	 */
	public NewGameController(GameRunner gameRunner) {
		if (gameRunner == null)
			throw new IllegalArgumentException("the specified argument cannot be null");
		this.gameRunner = gameRunner;
	}
	
	/**
	 * Create a new Race game on an automatically generated grid with specified
	 * dimensions.
	 * 
	 * @param width
	 *        The width of the game grid.
	 * @param height
	 *        The height of the game grid.
	 * @throws IllegalArgumentException
	 *         The specified width and height must be greater then the minima.
	 */
	public void newRaceGame(int width, int height) throws IllegalArgumentException {
		GameMode mode = new RaceMode();
		this.gameRunner.newGame(mode, width, height);
	}
	
	/**
	 * Create a new CTF game on an automatically generated grid with specified
	 * dimensions.
	 * 
	 * @param width
	 *        The width of the game grid.
	 * @param height
	 *        The height of the game grid.
	 * @param numberOfPlayers
	 *        The number of players that will play this game.
	 * @throws IllegalArgumentException
	 *         The specified width and height must be greater then the minima.
	 * @throws IllegalArgumentException
	 *         The number of players must be greater then or equal to
	 *         {@value CTFMode#MINIMUM_NUMBER_OF_PLAYERS}.
	 * @throws IllegalStateException
	 *         The number of players must be less then or equal to the number of
	 *         starting locations defined in grid to be created.
	 */
	public void newCTFGame(int width, int height, int numberOfPlayers)
			throws IllegalArgumentException {
		GameMode mode = new CTFMode(numberOfPlayers);
		this.gameRunner.newGame(mode, width, height);
	}
	
	/**
	 * Create a new Race game on on a grid loaded from the specified file.
	 * 
	 * @param file
	 *        The filepath of the grid-file.
	 * @throws InvalidGridFileException
	 *         The grid file is invalid
	 * @throws IllegalStateException
	 * @throws FileNotFoundException
	 *         The file was not found.
	 * @throws GridBuildException
	 *         The grid file must exist, adhere the correct rules and it cannot
	 *         contain invalid characters.
	 */
	public void newRaceGame(String file) throws InvalidGridFileException, IllegalStateException,
			FileNotFoundException {
		GameMode mode = new RaceMode();
		this.gameRunner.newGame(mode, file);
	}
	
	/**
	 * Create a new CTF game on on a grid loaded from the specified file.
	 * 
	 * @param file
	 *        The filepath of the grid-file.
	 * @param numberOfPlayers
	 *        the number of players in the game
	 * @throws InvalidGridFileException
	 *         The grid file is invalid.
	 * @throws FileNotFoundException
	 * @throws GridBuildException
	 *         The grid file must exist, adhere the correct rules and it cannot
	 *         contain invalid characters.
	 * @throws IllegalArgumentException
	 *         The number of players must be greater then or equal to
	 *         {@value CTFMode#MINIMUM_NUMBER_OF_PLAYERS}.
	 * @throws IllegalStateException
	 *         The number of players must be less then or equal to the number of
	 *         starting locations defined in grid to be created.
	 */
	public void newCTFGame(String file, int numberOfPlayers) throws InvalidGridFileException,
			IllegalStateException, FileNotFoundException {
		GameMode mode = new CTFMode(numberOfPlayers);
		this.gameRunner.newGame(mode, file);
	}
	
}
