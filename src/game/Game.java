package game;

import java.util.Observable;
import java.util.Observer;
import player.PlayerDataBase;

/**
 * TODO
 */
public class Game extends Observable implements Observer {
	
	private GameMode	mode;
	
	/**
	 * Creates a new Game with a given mode and grid.
	 * 
	 * @param mode
	 *        the mode for the new game
	 */
	public Game(GameMode mode) {
		if (mode == null)
			throw new IllegalArgumentException("the mode cannot be null");
		
		this.mode = mode;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof PlayerDataBase) {
			this.mode.checkWinner();
		}
		// else do nothing; return
	}
}
