package player;

import grid.Coordinate;
import grid.Grid;
import item.lightgrenade.LightGrenade;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import square.ISquare;
import square.Square;
import com.sun.istack.internal.NotNull;

/**
 * A class to store {@value #NUMBER_OF_PLAYERS} {@link Player}s and to appoint
 * the current player allowed to play. The {@link PlayerDataBase} will observe
 * his players. A Player notifies the database (by calling
 * <code>notifyObservers()</code>) to indicate he wants to end his turn.
 * 
 * At the same time, Squares will observe the PlayerDataBase, which will notify
 * them at each player change, to calculate how long they should stay power
 * failured.
 * 
 */
public class PlayerDataBase implements Observer, IPlayerDataBase {
	
	/**
	 * The number of players involved in the game.
	 */
	public static final int		NUMBER_OF_PLAYERS	= 2;
	
	@NotNull
	private ArrayList<Player>	playerList;
	private int					currentPlayerIndex;
	
	// The player database should not know the grid, but at this point it is
	// necessary to let it know that player switch has taken place.
	// TODO: fix this at some point.
	private Grid				grid;
	
	/**
	 * Creates a new empty PlayerDataBase. to fill the database with players,
	 * one has to call {@link PlayerDataBase#createNewDB(Coordinate[], Grid)
	 * createNewDB}. Until then the {@link PlayerDataBase#getCurrentPlayer()}
	 * method will throw an exception.
	 * 
	 * @param grid
	 *        The grid we will play with.
	 */
	public PlayerDataBase(Grid grid) {
		this.playerList = new ArrayList<Player>(NUMBER_OF_PLAYERS);
		this.grid = grid;
	}
	
	/**
	 * This method first clears the current database and then re-fills the
	 * database with PlayerDataBase.NUMBER_OF_PLAYERS newly created
	 * {@link Player} which it will observe.
	 * 
	 * The order of the players is determined by the specified starting
	 * positions array. The first player allowed to play, is the player with the
	 * first starting position in the specified array.
	 * 
	 * @param playerStartingPositions
	 *        The specified starting coordinates for the players to create
	 * 
	 * @param grid
	 *        The grid on which the players will move
	 * 
	 * @throws IllegalArgumentException
	 *         The arguments cannot be null. The lenght of the specified
	 *         playerStartingCoordinates array must be
	 *         {@value #NUMBER_OF_PLAYERS} and no two given coordinates can be
	 *         the same.
	 */
	public void createNewDB(@NotNull Coordinate[] playerStartingPositions, Grid grid)
			throws IllegalArgumentException {
		if (playerStartingPositions == null || grid == null) {
			throw new IllegalArgumentException("the given arguments cannot be null");
		}
		if (playerStartingPositions.length != NUMBER_OF_PLAYERS) {
			throw new IllegalArgumentException("The number of player-starting-coordinates is wrong");
		}
		if (!allDifferent(playerStartingPositions)) {
			throw new IllegalArgumentException(
					"The specified player-starting-coordinates must all be different");
		}
		
		this.grid = grid;
		
		Player.resetUniqueIdcounter();
		this.playerList.clear();
		for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
			Player newPlayer = new Player((Square) grid.getSquareAt(playerStartingPositions[i]));
			this.playerList.add(newPlayer);
			newPlayer.addObserver(this);
		}
		
		// Set the right upmost player as starting player.
		this.currentPlayerIndex = 0;
	}
	
	private boolean allDifferent(Coordinate[] playerStartingPositions) {
		for (int i = 0; i < playerStartingPositions.length - 1; i++) {
			Coordinate c1 = playerStartingPositions[i];
			for (int j = i + 1; j < playerStartingPositions.length; j++) {
				Coordinate c2 = playerStartingPositions[j];
				if (c1.equals(c2)) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public IPlayer getCurrentPlayer() throws IllegalStateException {
		if (this.playerList.size() == 0) {
			throw new IllegalStateException("The PlayerDatabase is empy.");
		}
		return this.playerList.get(this.currentPlayerIndex);
	}
	
	/**
	 * This method is called whenever an observed object (i.e. a {@link Player})
	 * notifies the database. A Player notifies the database to indicate he
	 * wants to end his turn. This happens if <li>he has no actions left</li>
	 * <li>he enters a {@link Square} with no power left and no active
	 * {@link LightGrenade}</li> <li>if the user explicitly asks to end the
	 * player's turn.</li> <br>
	 * 
	 * A {@link PlayerDataBase} stores PlayerDataBase.NUMBER_OF_PLAYERS
	 * {@link Player}s and appoints the current player allowed to play. The
	 * {@link PlayerDataBase} will observe his players. A Player notifies the
	 * database to indicate he wants to end his turn.
	 * 
	 * The database will only switch players if the player asking to end his
	 * turn is the current player ( <code>this.getCurrentPlayer</code>).
	 * 
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Player) {
			Player player = (Player) o;
			
			/*
			 * If a player for example enters a square with no power left and no
			 * active lightgrenade, he will ask the database to switch players.
			 * The player then decreases the number of actions done, this may
			 * become zero and thus the player will ask the database again to
			 * switch players. The database will not perform the second switch
			 * because it will see the player asking to end his turn is not the
			 * current player (which is switched in the first demand).
			 */
			if (player.equals(this.getCurrentPlayer())) {
				this.endCurrentPlayerTurn();
			}
		}
	}
	
	/**
	 * Ends the turn of the current player (this.getCurrentPlayer()) and
	 * appoints a next current player (circular shift).
	 */
	private void endCurrentPlayerTurn() {
		if (getCurrentPlayer().getCurrentLocation().equals(getFinishOfCurrentPlayer())) {
			System.out.println("Gewonnen!");
			// TODO: Notify game
		}
		
		this.currentPlayerIndex = (this.currentPlayerIndex + 1) % NUMBER_OF_PLAYERS;
		updatePowerFailures();
		
		// If the player is on a square with a power failure, it can do one
		// action less.
		if (getCurrentPlayer().getCurrentLocation().hasPowerFailure())
			getCurrentPlayer().skipNumberOfActions(1);
	}
	
	/**
	 * This method will notify the grid that a new turn started and it should
	 * upgrade power failures.
	 */
	private void updatePowerFailures() {
		grid.updatePowerFailures();
	}
	
	/**
	 * Gets the other player
	 * 
	 * @return The other player
	 */
	private Player getOtherPlayer() {
		return playerList.get((currentPlayerIndex + 1) % 2);
	}
	
	/**
	 * Returns the finish square of the current player, i.e. the starting square
	 * of the other player.
	 * 
	 * @return
	 */
	private ISquare getFinishOfCurrentPlayer() {
		return getOtherPlayer().getStartingPosition();
	}
}
