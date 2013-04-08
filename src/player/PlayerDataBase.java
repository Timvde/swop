package player;

import game.Game;
import grid.Grid;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Set;
import square.ISquare;
import square.Square;
import com.sun.istack.internal.NotNull;

/**
 * A class to store {@value #NUMBER_OF_PLAYERS} {@link Player}s and to appoint
 * the current player allowed to play. PlayerDataBase is an {@link Observable}
 * and will notify its observers any time a player switch has occurered.
 * 
 * {@link Grid} will observe the database in order to update the powerfailured
 * {@link Square}s. {@link Game} ass well will observe the database to be
 * notified when a Player has won/lost the game.
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
	 * Creates a new empty PlayerDataBase and calls the
	 * {@link #createNewDB(Set)} method with hte specified argument to fill it.
	 * 
	 * @param playerStartingPositions
	 *        The argument to pass to the {@link #createNewDB(Set)} method.
	 */
	public PlayerDataBase(@NotNull Set<Square> playerStartingPositions) {
		this.playerList = new ArrayList<Player>(NUMBER_OF_PLAYERS);
		this.createNewDB(playerStartingPositions);
	}
	
	/**
	 * This method first clears the current database and then re-fills the
	 * database with PlayerDataBase.NUMBER_OF_PLAYERS newly created
	 * {@link Player}s.
	 * 
	 * The order of the players is determined by the specified starting
	 * positions set. The first player allowed to play, is the player with the
	 * first starting position in the specified set.
	 * 
	 * @param playerStartingPositions
	 *        The specified starting coordinates for the players to create.
	 * 
	 * @throws IllegalArgumentException
	 *         The arguments cannot be null and the length of the specified
	 *         playerStartingCoordinates set must be {@value #NUMBER_OF_PLAYERS}
	 *         .
	 */
	public void createNewDB(@NotNull Set<Square> playerStartingPositions)
			throws IllegalArgumentException {
		if (playerStartingPositions == null)
			throw new IllegalArgumentException("the given argument cannot be null");
		if (playerStartingPositions.size() != NUMBER_OF_PLAYERS)
			throw new IllegalArgumentException("The number of player-starting-coordinates is wrong");
		
		Player.resetUniqueIdcounter();
		this.clearDataBase();
		
		for (Square square : playerStartingPositions) {
			Player newPlayer = new Player(square, this);
			this.playerList.add(newPlayer);
		}
		
		// Set the first player as starting player and assign him the turn.
		this.currentPlayerIndex = 0;
		this.playerList.get(currentPlayerIndex).assignNewTurn();
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
	
	@Override
	public IPlayer getCurrentPlayer() throws IllegalStateException {
		if (this.playerList.size() == 0)
			throw new IllegalStateException("The PlayerDatabase is empty.");
		
		return this.playerList.get(this.currentPlayerIndex);
	}
	
	/**
	 * End a players turn. This will set the state of the specified player to
	 * {@link PlayerState#WAITING} and set the next player to active. This next
	 * player then receives {@link Player.MAX_NUMBER_OF_ACTIONS_PER_TURN}
	 * actions for his turn. This method will also check whether the player has
	 * reached his finish position. If this is the case he will set the state of
	 * the player to finished and notify its observers (i.e. {@link Game}) that
	 * a player has finished the game.
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
			// set the current player to waiting
			player.setPlayerState(PlayerState.WAITING);
			
			// find the next player and assign him a new turn
			this.currentPlayerIndex = (this.currentPlayerIndex + 1) % NUMBER_OF_PLAYERS;
			player = playerList.get(currentPlayerIndex);
			
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
