package game;

import grid.Grid;
import grid.GridIterator;
import item.Flag;
import item.IItem;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import player.PlayerDataBase;
import player.TurnEvent;
import square.AbstractSquare;
import square.SquareContainer;
import square.StartingPositionProperty;

/**
 * A class representing a game. The lifetime of a Game-object equals that of the
 * game it represents. Each game is associated with an immutable
 * {@link GameMode mode}.
 * 
 * A Game observes the {@link PlayerDataBase database} to be informed of the
 * ending of player actions, wins and loses. One can observe a Game to be
 * notified when a player wins/loses and when a player's action ends.
 */
public class Game extends Observable implements Observer {
	
	private GameMode	mode;
	
	/**
	 * Creates a new Game with a given mode and grid.
	 * 
	 * @param mode
	 *        the mode for the new game
	 * @param grid
	 *        the grid to play this game on.
	 * @param playerDB
	 *        the playerdatabase the game will fill.
	 * @throws IllegalStateException
	 *         The number of players as {@link GameMode#getNumberOfPlayers()
	 *         given by the mode} must be less then or equal to the number of
	 *         starting locations defined in the grid.
	 */
	public Game(GameMode mode, Grid grid, PlayerDataBase playerDB) {
		if (mode == null || grid == null || playerDB == null)
			throw new IllegalArgumentException("the args cannot be null");
		if (grid.getAllStartingPositions().size() < mode.getNumberOfPlayers())
			throw new IllegalStateException(
					"The number of players must be less then or equal to the number of starting locations defined in the grid");
		
		this.mode = mode;
		
		// unwrap the superfluous playerstarts
		List<SquareContainer> list = grid.getAllStartingPositions();
		for (int i = mode.getNumberOfPlayers(); i < list.size(); i++)
			list.get(i).removeProperty(new StartingPositionProperty());
		
		list = list.subList(0, mode.getNumberOfPlayers());
		
		playerDB.createNewDB(list);
		fixObserversPlayerDB(grid, playerDB);
		
		// Remove the flags of the players that don't play. This is the case when the number
		// of players chosen is not the same as the number of players in the grid file.
		GridIterator gridIterator = (GridIterator) grid.getGridIterator();
		while (gridIterator.hasNext()) {
			SquareContainer square = gridIterator.next();
			
			for (IItem item : square.getAllItems()) {
				if ((item instanceof Flag) && !square.hasPlayer()) {
					square.remove(item);
				}
			}
		}
	}
	
	private void fixObserversPlayerDB(Grid grid, PlayerDataBase playerDB) {
		playerDB.deleteObservers();
		// make all the squares in the new grid observer of the db
		Iterator<SquareContainer> gridIterator = grid.getGridIterator();
		while (gridIterator.hasNext())
			playerDB.addObserver(gridIterator.next());
		playerDB.addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof PlayerDataBase && arg instanceof TurnEvent) {
			PlayerDataBase playerDB = (PlayerDataBase) o;
			TurnEvent turnEvent = (TurnEvent) arg;
			
			if (this.mode.checkCurrentPlayerHasWon(playerDB, turnEvent)) {
				this.setChanged();
				this.notifyObservers(GameEvent.PLAYER_WON);
			}
			else if (this.mode.checkCurrentPlayerHasLost(playerDB, turnEvent)) {
				this.setChanged();
				this.notifyObservers(GameEvent.PLAYER_LOSE);
			}
		}
		// else do nothing; return
	}
	
}
