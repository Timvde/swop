package game;

import grid.Grid;
import java.util.Observable;
import player.IPlayer;
import actions.Action;
import actions.GridAction;

public class Game extends Observable {
	
	private Grid	grid;
	private IPlayer	currentPlayer;
	
	public void startNewGame() {
		
	}
	
	public void executeAction(Action action) {
		
		this.hasChanged();
		this.notifyObservers();
	}
	
	public void executeGridAction(GridAction action) {
		action.setGrid(grid);
		action.setPlayer(currentPlayer);
		action.execute();
		this.setChanged();
		this.notifyObservers();
	}
	
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	public void setCurrentPlayer(IPlayer player) {
		this.currentPlayer = player;
	}
	
	public IPlayer getCurrentPlayer() {
		return this.currentPlayer;
	}
}
