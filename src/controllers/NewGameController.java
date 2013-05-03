package controllers;

import game.Game;

/**
 * A controller for handling the GUI new game commands.
 * 
 * @author tom
 *
 */
public class NewGameController {
	
	private Game game;
	
	/**
	 * Create a new game controller with a given game.
	 * 
	 * @param g
	 * 			The game reference.
	 */
	public NewGameController(Game g) {
		this.game = g;
	}
	
	/**
	 * Create a new game with given dimensions.
	 * 
	 * @param width
	 * 			The width of the game grid.
	 * @param height
	 * 			The height of the game grid.
	 */
	public void newGame(int width, int height) {
		this.game.newGame(width, height);
	}
	
	/**
	 * Create a new game that is read from a file.
	 * 
	 * @param file
	 * 			The file the grid is located in.
	 */
	public void newGame(String file) {
		this.game.newGameFromFile(file);
	}
}
