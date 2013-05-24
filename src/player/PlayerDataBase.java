package player;

import game.Game;
import grid.Grid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import square.PropertyType;
import square.Square;
import square.SquareContainer;

/**
 * A class to store {@link TronPlayer players}, to appoint the current player
 * allowed to play and to manage his number of alllowed actions. PlayerDataBase
 * is an {@link Observable} and will notify its observers (passing the
 * {@link PlayerState} of the player whos turn is ended as an argument) any time
 * a player switch has occurered.
 * 
 * {@link Grid} will observe the database in order to update the powerfailured
 * {@link Square}s. {@link Game} as well will observe the database to be
 * notified when a Player has won/lost the game.
 * 
 * TODO update doc
 */
public class PlayerDataBase extends Observable implements IPlayerDataBase {
	
	private List<TronPlayer>	playerList;
	private int					currentPlayerIndex;
	
	/**
	 * Creates a new empty PlayerDataBase. To fill the database with players,
	 * one has to call {@link PlayerDataBase#createNewDB(List) createNewDB}.
	 * Until then the {@link PlayerDataBase#getCurrentPlayer()} method will
	 * throw an exception.
	 */
	public PlayerDataBase() {
		this.playerList = new ArrayList<TronPlayer>();
	}
	
	/**
	 * This method creates a new database with {@link TronPlayer players} with
	 * the specified starting positions. The Players will all have the
	 * {@link PlayerState#WAITING} state.
	 * 
	 * The order of the players is determined by the specified starting
	 * positions list. I.e. the first player allowed to play, is the first
	 * player in the list and so on.
	 * 
	 * @param startingPositions
	 *        The startingpositions for the players to create.
	 * @throws IllegalArgumentException
	 *         The list of starting positions must be valid, i.e.
	 *         <code>{@link #isValidStartingPositionList(List)}</code>
	 */
	public void createNewDB(List<SquareContainer> startingPositions)
			throws IllegalArgumentException {
		if (!isValidStartingPositionList(startingPositions))
			throw new IllegalArgumentException("the list of starting positions must be valid");
		
		TronPlayer.resetUniqueIdcounter();
		this.clearDataBase();
		
		for (SquareContainer playerStartingPosition : startingPositions) {
			TronPlayer newPlayer = new TronPlayer(this, playerStartingPosition);
			this.playerList.add(newPlayer);
		}
		
		// Set the first player as starting player.
		this.currentPlayerIndex = 0;
		assignNewTurn(playerList.get(currentPlayerIndex));
	}
	
	/**
	 * Returns whether or not the specified startingpositions list is valid.
	 * I.e. : <li>It cannot be <code>null</code></li> <li>It must contain at
	 * least one position</li> <li>There can be no duplicates in the
	 * startingpositions</li> <li>All the squares must be a
	 * starting position</li>
	 * 
	 * @param startingPositions
	 *        the list to test.
	 * @return Whether this is a valid startingpostions list.
	 */
	public boolean isValidStartingPositionList(List<SquareContainer> startingPositions) {
		if (startingPositions == null)
			return false;
		if (startingPositions.size() == 0)
			return false;
		if (new HashSet<SquareContainer>(startingPositions).size() != startingPositions.size())
			return false;
		for (SquareContainer squareContainer : startingPositions)
			if (!(squareContainer.hasProperty(PropertyType.STARTING_POSITION)))
				return false;
		
		return true;
	}
	
	@Override
	public TronPlayer getCurrentPlayer() throws IllegalStateException {
		if (getNumberOfPlayers() == 0)
			throw new IllegalStateException("The PlayerDatabase is empty.");
		
		return this.playerList.get(this.currentPlayerIndex);
	}
	
	/**
	 * Returns a list of all the players in the database.
	 * 
	 * @return a list of all the players in the database.
	 */
	public List<Player> getAllPlayers() {
		return new ArrayList<Player>(this.playerList);
	}
	
	/**
	 * The total number of players in the database. Same ad
	 * <code>{@link #getAllPlayers()}.size()</code>
	 * 
	 * @return The total number of players in the database.
	 */
	public int getNumberOfPlayers() {
		return this.playerList.size();
	}
	
	/**
	 * Ends a player's turn. If the specified player is not the current player
	 * (i.e. {@link #getCurrentPlayer()}), this method will return. Else it will
	 * set the state of the specified player to {@link PlayerState#WAITING} and
	 * set the next player to {@link PlayerState#ACTIVE}. This next player then
	 * receives {@value TronPlayer#MAX_NUMBER_OF_ACTIONS_PER_TURN} actions for
	 * his turn.
	 * 
	 * This method will also check whether the player has reached his finish
	 * position. If this is the case he will set the state of the player to
	 * finished.
	 * 
	 * Finally this method will notify the observers (passing the
	 * {@link PlayerState} of the player whose turn is ended as an argument) to
	 * indicate a Player switch has occurred.
	 * 
	 * @param player
	 *        The player who wants to end his turn. Only the current player can
	 *        do this.
	 */
	private void endPlayerTurn(TronPlayer player) {
		if (!player.equals(getCurrentPlayer())) {
			// Only the current player can end its turn. By returning instead of
			// throwing, we actually handle this problem correctly without
			// having to catch an exception on other places.
			return;
		}
		notifyTurnEvent(TurnEvent.END_TURN);
		
		// Switch players and assign a new turn
		player.getActionManager().resetActions();
		player.setPlayerState(PlayerState.WAITING);
		this.currentPlayerIndex = (this.currentPlayerIndex + 1) % getNumberOfPlayers();
		TronPlayer newPlayer = playerList.get(currentPlayerIndex);
		
		// Assign new actions to the specified player and set him active.
		// This may introduce a new player switch (the resulting penalty
		// after adding new actions may still be < 0)
		assignNewTurn(newPlayer);
	}
	
	private void notifyTurnEvent(TurnEvent event) {
		this.setChanged();
		this.notifyObservers(event);
	}
	
	/**
	 * This method will under normal circumstances increase the allowed number
	 * of actions left with {@link #MAX_NUMBER_OF_ACTIONS_PER_TURN}.
	 * 
	 * When this player is on a power failured square, the number of actions
	 * lost will be {@value #POWER_FAILURE_PENALTY_AT_START_TURN}.
	 * 
	 * This method is called by the {@link PlayerDataBase} when it assigns the
	 * next player.
	 */
	void assignNewTurn(TronPlayer player) {
		player.setPlayerState(PlayerState.ACTIVE);		
		player.getActionManager().assignNewTurn();
		
		// allowed number of actions could be <= 0 because of penalties
		checkEndTurn(player);
	}
	
	/**
	 * This method checks if a player has any actions left. If not, it ends its
	 * turn.
	 */
	private void checkEndTurn(TronPlayer player) {
		if (player.getAllowedNumberOfActions() <= 0) {
			// this method will return if it's not this player's turn
			endPlayerTurn(player);
		}
	}
	
	/**
	 * Called when a player performed an action, his allowed number of actions
	 * must drop by one.
	 * 
	 * If after decreasing the allowed number of actions by one, this player has
	 * no more actions left, his turn will end.
	 */
	void actionPerformed(TronPlayer player) {
		if (player.equals(getCurrentPlayer())) {
			notifyTurnEvent(TurnEvent.END_ACTION);
			checkEndTurn(player);
		}
	}
	
	/**
	 * Clears the current DB. Destroys all the players within so that no-one can
	 * use an old Player reference to break the game.
	 */
	public void clearDataBase() {
		for (TronPlayer player : this.playerList) {
			// set all references of the player to null so that no-one can still
			// play with an old player.
			player.endPlayerLife();
		}
		this.playerList.clear();
	}
	
	/**
	 * Gets the next player
	 * 
	 * <b>FOR TESTING PURPOSES ONLY</b>
	 * 
	 * @return The next player
	 */
	TronPlayer getNextPlayer() {
		return playerList.get((currentPlayerIndex + 1) % getNumberOfPlayers());
	}
	
	/**
	 * The player dies and is removed from the game and disappears. All items he
	 * possessed disappear.
	 */
	public void removeCurrentPlayer() {
		TronPlayer curPlayer = this.playerList.get(currentPlayerIndex);
		curPlayer.endPlayerLife();
		this.playerList.remove(curPlayer);
		this.currentPlayerIndex = (this.currentPlayerIndex + 1) % getNumberOfPlayers();
	}
}
