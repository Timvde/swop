package player;

import game.Game;
import grid.Grid;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import square.Square;
import square.NormalSquare;

/**
 * A class to store {@value #NUMBER_OF_PLAYERS} {@link Player}s and to appoint
 * the current player allowed to play. PlayerDataBase is an {@link Observable}
 * and will notify its observers (passing the {@link PlayerState} of the player
 * whos turn is ended as an argument) any time a player switch has occurered.
 * 
 * {@link Grid} will observe the database in order to update the powerfailured
 * {@link NormalSquare}s. {@link Game} ass well will observe the database to be
 * notified when a Player has won/lost the game.
 */
public class PlayerDataBase extends Observable implements IPlayerDataBase {
	
	/**
	 * The number of players involved in the game.
	 */
	public static final int		NUMBER_OF_PLAYERS	= 2;
	
	private ArrayList<Player>	playerList;
	private int					currentPlayerIndex;
	
	/**
	 * Creates a new empty PlayerDataBase. To fill the database with players,
	 * one has to call {@link PlayerDataBase#createNewDB() createNewDB}. Until
	 * then the {@link PlayerDataBase#getCurrentPlayer()} method will throw an
	 * exception.
	 */
	public PlayerDataBase() {
		this.playerList = new ArrayList<Player>(NUMBER_OF_PLAYERS);
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
		}
		// Set the first player as starting player.
		this.currentPlayerIndex = 0;
		
		return playerList;
	}
	
	@Override
	public IPlayer getCurrentPlayer() throws IllegalStateException {
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
			this.notifyObservers(player.getPlayerState());
		}
		else {
			// Switch players and assign a new turn
			player.setPlayerState(PlayerState.WAITING);
			this.currentPlayerIndex = (this.currentPlayerIndex + 1) % NUMBER_OF_PLAYERS;
			Player newPlayer = playerList.get(currentPlayerIndex);
			
			this.setChanged();
			this.notifyObservers(player.getPlayerState());
			
			// Assign new actions to the specified player and set him active.
			// This may introduce a new player switch (the resulting penalty
			// after adding new actions may still be < 0)
			newPlayer.assignNewTurn();
		}
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
	private Square getFinishOfCurrentPlayer() {
		return getOtherPlayer().getStartingPosition();
	}
	
	/**
	 * This method reports the db that the specified player has received a
	 * {@link StartDocument} position. This means he's ready to
	 * {@link Player#assignNewTurn() start playing}.
	 * 
	 * @param p
	 *        The player reporting he's ready to start playing
	 */
	void reportReadyToStart(Player p) {
		if (p.getCurrentLocation() == null) {
			throw new IllegalStateException("looks like the player didn't receive start position");
		}
		
		if (p.equals(getCurrentPlayer())) {
			p.assignNewTurn();
		}
	}
}
