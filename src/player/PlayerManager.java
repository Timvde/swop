package player;

import grid.Coordinate;

import java.util.ArrayList;
import java.util.Map;

import ObjectronExceptions.IllegalNumberOfPlayersException;

import com.sun.istack.internal.NotNull;


public class PlayerManager {

	private static final int NUMBER_OF_PLAYERS = 2;

	@NotNull
	private Map<Player, Coordinate> playerCoordMap;

	@NotNull
	private ArrayList<Player> playerList = new ArrayList<Player>();

	private int currentPlayerIndex; // index in playerList

	private int currentNumberOfActions;

	/**
	 * Creates a new Playermanager with a given player-coordinate map. The order
	 * of the player-turns is defined by
	 * <code>playerCoordinateMap.keySet().toArray()</code>
	 * 
	 * @param playerList
	 *            the given player-coordinate map
	 * @throws IllegalNumberOfPlayersException
	 *             The given player-coordinate map must have exactly
	 *             PlayerManager.NUMBER_OF_PLAYERS entries
	 */
	public PlayerManager(Map<Player, Coordinate> playerCoordinateMap)
			throws IllegalNumberOfPlayersException {
		if (playerCoordMap.size() != NUMBER_OF_PLAYERS)
			throw new IllegalNumberOfPlayersException();

		this.playerCoordMap = playerCoordinateMap;
		this.playerList.addAll(playerCoordinateMap.keySet());

		this.currentPlayerIndex = 0;
		this.currentNumberOfActions = 0;
	}

	/**
	 * Return the Coordinate of the current player.
	 * 
	 * @return A Coordinate object that is the coordinate of the current player.
	 */
	public Coordinate getCurrentPlayerCoordinate() {
		return this.playerCoordMap.get(this.getCurrentPlayer());
	}

	/**
	 * Returns the player who is currently allowed to play.
	 * 
	 * @return the player who is currently allowed to play.
	 */
	public Player getCurrentPlayer() {
		return this.playerList.get(this.currentPlayerIndex);
	}

	/**
	 * Ends the turn of the current player (this.getCurrentPlayer()) and
	 * appoints a next current player (round robin).
	 */
	public void endCurrentPlayerTurn() {
		if (this.currentPlayerIndex < this.playerList.size())
			this.currentPlayerIndex++;
		else
			this.currentPlayerIndex = 0;
		
		this.currentNumberOfActions = 0;
	}
}
