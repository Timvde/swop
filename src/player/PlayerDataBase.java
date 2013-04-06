package player;

import game.Game;
import java.util.ArrayList;
import java.util.Observable;
import square.ASquare;
import square.ISquare;
import square.Square;
import com.sun.istack.internal.NotNull;

/**
 * A class to store {@value #NUMBER_OF_PLAYERS} {@link Player}s and to appoint
 * the current player allowed to play.
 * 
 * 
 * FIXME UPDATE THIS COMMENT The {@link PlayerDataBase} will observe his
 * players. A Player notifies the database (by calling
 * <code>notifyObservers()</code>) to indicate he wants to end his turn.
 * 
 * At the same time, Squares will observe the PlayerDataBase, which will notify
 * them at each player change, to calculate how long they should stay power
 * failured.
 * 
 */
public class PlayerDataBase extends Observable implements IPlayerDataBase {
	
	/**
	 * The number of players involved in the game.
	 */
	public static final int		NUMBER_OF_PLAYERS	= 2;
	
	@NotNull
	private ArrayList<Player>	playerList;
	private int					currentPlayerIndex;
	
	/**
	 * Creates a new empty PlayerDataBase. to fill the database with players,
	 * one has to call {@link PlayerDataBase#createNewDB(ASquare[]) createNewDB}
	 * . Until then the {@link PlayerDataBase#getCurrentPlayer()} method will
	 * throw an exception.
	 */
	// TODO: Why do we not add an argument to the constructor and call the
	// createNewDB method from within?
	public PlayerDataBase() {
		this.playerList = new ArrayList<Player>(NUMBER_OF_PLAYERS);
	}
	
	/**
	 * This method first clears the current database and then re-fills the
	 * database with PlayerDataBase.NUMBER_OF_PLAYERS newly created
	 * {@link Player}s.
	 * 
	 * The order of the players is determined by the specified starting
	 * positions array. The first player allowed to play, is the player with the
	 * first starting position in the specified array.
	 * 
	 * @param playerStartingPositions
	 *        The specified starting coordinates for the players to create
	 * 
	 * 
	 * @throws IllegalArgumentException
	 *         The arguments cannot be null. The length of the specified
	 *         playerStartingCoordinates array must be
	 *         {@value #NUMBER_OF_PLAYERS} and no two given coordinates can be
	 *         the same.
	 */
	// who the f**k uses an array these days?
	// And if you wanted all different values, just use a set {@link
	// java.util.Set} !!
	// can you please change this, i expect a bit more professionalism in this
	// project
	public void createNewDB(@NotNull ASquare[] playerStartingPositions)
			throws IllegalArgumentException {
		if (playerStartingPositions == null)
			throw new IllegalArgumentException("the given arguments cannot be null");
		if (playerStartingPositions.length != NUMBER_OF_PLAYERS)
			throw new IllegalArgumentException("The number of player-starting-coordinates is wrong");
		
		if (!allDifferent(playerStartingPositions))
			throw new IllegalArgumentException(
					"The specified player-starting-coordinates must all be different");
		
		Player.resetUniqueIdcounter();
		this.clearDataBase();
		for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
			Player newPlayer = new Player((Square) playerStartingPositions[i], this);
			this.playerList.add(newPlayer);
		}
		
		// Set the first player as starting player and make him active.
		this.currentPlayerIndex = 0;
		this.playerList.get(currentPlayerIndex).setPlayerState(PlayerState.ACTIVE);
	}
	
	/**
	 * Clears the current DB. Destroys all the players within so that no-one can
	 * use an old Player reference to break the game.
	 */
	private void clearDataBase() {
		for (Player p : this.playerList) {
			// set all references of the player to null so that no-one can still
			// play with an old player.
			p.destroy();
		}
		this.playerList.clear();
	}
	
	private boolean allDifferent(ASquare[] playerStartingPositions) {
		for (int i = 0; i < playerStartingPositions.length - 1; i++) {
			ASquare c1 = playerStartingPositions[i];
			for (int j = i + 1; j < playerStartingPositions.length; j++) {
				ASquare c2 = playerStartingPositions[j];
				if (c1.equals(c2)) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public IPlayer getCurrentPlayer() throws IllegalStateException {
		if (this.playerList.size() == 0)
			throw new IllegalStateException("The PlayerDatabase is empy.");
		
		return this.playerList.get(this.currentPlayerIndex);
	}
	
	/**
	 * End a players turn. This will set the state of the specified player to
	 * {@link PlayerState#WAITING} and set the next player to active. This next
	 * player then receives three actions for his turn. This method will also
	 * check whether the player has reached his finish position. If this is the
	 * case he will set the state of the player to finished and notify its
	 * observers (i.e. {@link Game}) that a player has finished the game.
	 * 
	 * @param player
	 *        the player who wants to end his turn
	 * 
	 * @throws IllegalStateException
	 *         Only the current player (i.e. {@link #getCurrentPlayer()}) can
	 *         switch the players.
	 */
	void endPlayerTurn(Player player) throws IllegalStateException {
		if (!player.equals(getCurrentPlayer())) {
			throw new IllegalStateException("Only the current player can end his turn");
		}
		
		if (player.getCurrentLocation().equals(getFinishOfCurrentPlayer())) {
			player.setPlayerState(PlayerState.FINISHED);
			// TODO notify Game
			this.setChanged();
			this.notifyObservers();
		}
		else {
			
			// check if the current player can be set to waiting 
			if (!player.getPlayerState().canTransistionTo(PlayerState.WAITING))
				throw new IllegalStateException("It looks like the game has finished already");
			
			// set the current player to waiting
			player.setPlayerState(PlayerState.WAITING);
			
			// find the next player and assign him a new turn
			this.currentPlayerIndex = (this.currentPlayerIndex + 1) % NUMBER_OF_PLAYERS;
			player = playerList.get(currentPlayerIndex);
			
			// check if the next player can start his turn 
			if (! player.getPlayerState().canTransistionTo(PlayerState.ACTIVE))
				throw new IllegalStateException("It looks like the game has finished already");
			
			// assign new actions to the specified player and set him active
			player.assignNewTurn();
			
			// notify observers a Player-change has occured
			this.setChanged();
			this.notifyObservers();
			
			// updatePowerFailures();
		}
	}
	
	/**
	 * This method will notify the grid that a new turn started and it should
	 * upgrade power failures.
	 */
	@Override
	public void notifyObservers() {
		this.setChanged();
		super.notifyObservers();
	}
	
	/**
	 * Gets the other player
	 * 
	 * @return The other player
	 */
	private Player getOtherPlayer() {
		return playerList.get((currentPlayerIndex + 1) % NUMBER_OF_PLAYERS);
	}
	
	/**
	 * Returns the finish square of the current player, i.e. the starting square
	 * of the other player.
	 * 
	 * @return The finish square of the current player.
	 */
	private ISquare getFinishOfCurrentPlayer() {
		return getOtherPlayer().getStartingPosition();
	}
}
