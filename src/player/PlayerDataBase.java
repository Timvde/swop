package player;

import game.Game;
import grid.Grid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import square.ISquare;
import square.Square;

/**
 * A class to store {@value #NUMBER_OF_PLAYERS} {@link Player}s and to appoint
 * the current player allowed to play. PlayerDataBase is an {@link Observable}
 * and will notify its observers (passing the {@link PlayerState} of the player
 * whos turn is ended as an argument) any time a player switch has occurered.
 * 
 * {@link Grid} will observe the database in order to update the powerfailured
 * {@link Square}s. {@link Game} ass well will observe the database to be
 * notified when a Player has won/lost the game.
 */
public class PlayerDataBase extends Observable implements IPlayerDataBase {
	
	/**
	 * The number of players involved in the game.
	 */
	public static final int			NUMBER_OF_PLAYERS					= 2;
	
	/** The maximum number of actions a Player is allowed to do in one turn */
	public static final int			MAX_NUMBER_OF_ACTIONS_PER_TURN		= 3;
	
	/**
	 * The number of actions that a player loses if he is standing on a power
	 * failured square at the start of his turn.
	 */
	private static final int		POWER_FAILURE_PENALTY_AT_START_TURN	= 1;
	
	private List<Player>			playerList;
	private int						currentPlayerIndex;
	private Map<Player, Integer>	actionsLeft;
	
	/**
	 * Creates a new empty PlayerDataBase. To fill the database with players,
	 * one has to call {@link PlayerDataBase#createNewDB() createNewDB}. Until
	 * then the {@link PlayerDataBase#getCurrentPlayer()} method will throw an
	 * exception.
	 */
	public PlayerDataBase() {
		this.playerList = new ArrayList<Player>(NUMBER_OF_PLAYERS);
		this.actionsLeft = new HashMap<Player, Integer>();
	}
	
	/**
	 * This method first clears the current database and then re-fills the
	 * database with {@value #NUMBER_OF_PLAYERS} newly created {@link Player}s.
	 * The Players will all have the {@link PlayerState#WAITING} state and will
	 * have no starting position.
	 * 
	 * @return The newly created list of players.
	 */
	public List<Player> createNewDB() {
		Player.resetUniqueIdcounter();
		this.clearDataBase();
		for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
			Player newPlayer = new Player(this);
			this.playerList.add(newPlayer);
			this.actionsLeft.put(newPlayer, 0);
		}
		// Set the first player as starting player.
		this.currentPlayerIndex = 0;
		
		return playerList;
	}
	
	@Override
	public Player getCurrentPlayer() throws IllegalStateException {
		if (this.playerList.size() == 0)
			throw new IllegalStateException("The PlayerDatabase is empty.");
		
		return this.playerList.get(this.currentPlayerIndex);
	}
	
	/**
	 * Ends a player's turn. If the specified player is not the current player
	 * (i.e. {@link #getCurrentPlayer()}), this method will return. Else it will
	 * set the state of the specified player to {@link PlayerState#WAITING} and
	 * set the next player to {@link PlayerState#ACTIVE}. This next player then
	 * receives {@value Player#MAX_NUMBER_OF_ACTIONS_PER_TURN} actions for his
	 * turn.
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
	void endPlayerTurn(Player player) {
		if (!player.equals(getCurrentPlayer())) {
			// Only the current player can end its turn. By returning instead of
			// throwing, we actually handle this problem correctly without
			// having to catch an exception on other places.
			return;
		}
		
		if (player.getCurrentLocation().equals(getFinishOfCurrentPlayer())) {
			player.setPlayerState(PlayerState.FINISHED);
			
			this.setChanged();
			this.notifyObservers(TurnEvent.END_GAME);
		}
		else {
			// Switch players and assign a new turn
			player.setPlayerState(PlayerState.WAITING);
			this.currentPlayerIndex = (this.currentPlayerIndex + 1) % NUMBER_OF_PLAYERS;
			Player newPlayer = playerList.get(currentPlayerIndex);
			
			this.setChanged();
			this.notifyObservers(TurnEvent.END_TURN);
			
			// Assign new actions to the specified player and set him active.
			// This may introduce a new player switch (the resulting penalty
			// after adding new actions may still be < 0)
			assignNewTurn(newPlayer);
		}
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
	// only the DB should assign turns --> package access
	void assignNewTurn(Player player) {
		// rest the turn related properties
		player.resetHasMoved();
		player.setPlayerState(PlayerState.ACTIVE);
		
		int allowedNumberOfActionsLeft = getAllowedNumberOfActions(player);
		
		if (player.getCurrentLocation().hasPowerFailure()) {
			/*
			 * decrease the allowed number of actions by the right amount (do
			 * not use the method skipNumberOfActions() as this will call
			 * checkEndTurn() and thus the checkEndTurn() below might throw an
			 * exception
			 */
			allowedNumberOfActionsLeft -= POWER_FAILURE_PENALTY_AT_START_TURN;
		}
		
		setAllowedNumberOfActions(player, Math.min(allowedNumberOfActionsLeft
				+ MAX_NUMBER_OF_ACTIONS_PER_TURN, MAX_NUMBER_OF_ACTIONS_PER_TURN));
		
		// allowed number of actions could be <= 0 because of penalties
		checkEndTurn(player);
	}
	
	/**
	 * This method checks if a player has any actions left. If not, it ends its
	 * turn.
	 */
	private void checkEndTurn(Player player) {
		if (getAllowedNumberOfActions(player) <= 0) {
			// this method will return if it's not this player's turn
			endPlayerTurn(player);
		}
	}
	
	/**
	 * This method lets a player skip a number of actions. He doesn't need to be
	 * the active player at this moment.
	 * 
	 * @param player
	 *        The player which needs to skip actions.
	 * @param numberOfActionsToSkip
	 *        The number of actions to skip
	 */
	public void skipNumberOfActions(Player player, int numberOfActionsToSkip) {
		int numberOfActions = getAllowedNumberOfActions(player) - numberOfActionsToSkip;
		setAllowedNumberOfActions(player, numberOfActions);
		
		checkEndTurn(player);
	}
	
	private void setAllowedNumberOfActions(Player player, int numberOfActions) {
		actionsLeft.put(player, numberOfActions);
	}
	
	int getAllowedNumberOfActions(Player player) {
		return actionsLeft.get(player);
	}
	
	/**
	 * This method is used when a player wants to tell he has lost the game
	 * (e.g. by ending his turn before doing a move).
	 * 
	 * @param player
	 *        The player telling he has lost the game
	 * 
	 * @throws IllegalStateException
	 *         The player who tells he has lost the game must have a
	 *         {@link PlayerState#LOST} state.
	 */
	void reportGameLost(Player player) {
		if (player.getPlayerState() != PlayerState.LOST) {
			throw new IllegalStateException(
					"Looks like the player asking to lose the game hasn't lost after all");
		}
		this.setChanged();
		this.notifyObservers(player.getPlayerState());
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
	
	/**
	 * Gets the other player
	 * 
	 * FOR TESTING PURPOSES ONLY
	 * 
	 * @return The other player
	 */
	Player getOtherPlayer() {
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
	
	/**
	 * This method reports the db that the specified player has received a
	 * {@link StartDocument} position. This means he's ready to
	 * {@link Player#assignNewTurn() start playing}.
	 * 
	 * @param player
	 *        The player reporting he's ready to start playing
	 */
	void reportReadyToStart(Player player) {
		if (player.getCurrentLocation() == null) {
			throw new IllegalStateException("looks like the player didn't receive start position");
		}
		
		if (player.equals(getCurrentPlayer())) {
			assignNewTurn(player);
		}
	}
}
