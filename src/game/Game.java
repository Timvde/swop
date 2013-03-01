package game;


import grid.Grid;
import java.util.Observable;
import actions.Action;

public class Game extends Observable {
	
	private Grid	grid;
	
	public void startNewGame() {
		
	}
	
	public void excuteAction(Action action) {
		
		this.hasChanged();
		this.notifyObservers();
	}
	
	
}
