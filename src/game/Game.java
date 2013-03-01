package game;

import grid.Grid;
import java.util.Observable;
import player.IPlayer;
import actions.Action;
import actions.GridAction;

/**
 * The game class contains all information about the game. Controllers are able
 * to pass actions to the game, which executes them if they don't violate any
 * rules.
 * 
 * @author tom
 */
public class Game extends Observable {
	
	private Grid	grid;
	private IPlayer	currentPlayer;
	
	/**
	 * TODO
	 */
	public void startNewGame() {
		
	}
	
	/**
	 * Execute an action and notify the observers.
	 * 
	 * @param action
	 *        The action that will be executed.
	 */
	public void executeAction(Action action) {
		
		this.hasChanged();
		this.notifyObservers();
	}
	
	/**
	 * Execute an action on the Grid and notify the observers.
	 * 
	 * @param action
	 * 			The action that will be executed.
	 */
	public void executeGridAction(GridAction action) {
		action.setGrid(grid);
		action.setPlayer(currentPlayer);
		action.execute();
		this.setChanged();
		this.notifyObservers();
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
}
