package game;

import grid.Grid;
import player.IPlayer;
import player.Player;

/**
 * The game class contains all information about the game. Controllers are able
 * to pass actions to the game, which executes them if they don't violate any
 * rules.
 * 
 * @author tom
 */
public class Game implements IGame {
	
	private Grid	grid;
	private IPlayer	currentPlayer;

	/**
	 * TODO
	 */
	public void startNewGame() {
		
	}
	
	/**
	 * Set the grid of the game.
	 * @param grid
	 * 			The grid to set.
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	/**
	 * Set the current player of the game.
	 * @param player
	 * 			The player to set as current player.
	 */
	public void setCurrentPlayer(IPlayer player) {
		this.currentPlayer = player;
	}
	
	/**
	 * Return the current player of the game.
	 */
	public IPlayer getCurrentPlayer() {
		return this.currentPlayer;
	}

	@Override
	public void newGame(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endGame(Player p) {
		// TODO Auto-generated method stub
		
	}
}
