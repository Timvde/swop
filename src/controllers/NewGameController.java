package controllers;

import game.Game;
import game.GameRunner;
import ObjectronExceptions.builderExceptions.GridBuildException;

/**
 * A controller for handling the GUI new game commands.
 * 
 * @author tom
 * 
 */
public class NewGameController {
	
	private GameRunner	game;
	
	/**
	 * Create a new game controller with a given game.
	 * 
	 * @param gameRunner
	 *        The game reference.
	 */
	public NewGameController(GameRunner gameRunner) {
		if (gameRunner == null)
			throw new IllegalArgumentException("the specified argument cannot be null");
		this.game = gameRunner;
	}
	
	/**
	 * Create a new game on an automatically generated grid with specified
	 * dimensions.
	 * 
	 * @param width
	 *        The width of the game grid.
	 * @param height
	 *        The height of the game grid.
	 * @throws IllegalArgumentException
	 *         The specified width and height must be greater then the minima.
	 */
	public void newGame(int width, int height) throws IllegalArgumentException {
		this.game.newGame(width, height);
	}
	
	/**
	 * Create a new game on a grid loaded from the specified file.
	 * 
	 * @param file
	 *        The file the grid is located in.
	 * @throws GridBuildException
	 *         The grid file must exist, adhere the correct rules and it cannot contain
	 *         invalid characters.
	 */
	public void newGame(String file) throws GridBuildException {
		this.game.newGameFromFile(file);
	}
	
	
}
