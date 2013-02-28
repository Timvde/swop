package player;

import grid.Coordinate;

import java.util.Map;

import ObjectronExceptions.IllegalNumberOfPlayersException;

import com.sun.istack.internal.NotNull;

public class PlayerManager {

	public static final int NUMBER_OF_PLAYERS = 2;
	
	@NotNull
	private Map<Player, Coordinate> playerList;

	@NotNull
	private Player currentPlayer;

	int currentNumberOfActions;

	/**
	 * Creates a new Playermanager with a given player-coordinate map and takes
	 * the first player as the starting player.
	 * 
	 * @param playerList
	 *            the given player-coordinate map
	 * @throws IllegalNumberOfPlayersException
	 *             The given player-coordinate map must have exactly
	 *             PlayerManager.NUMBER_OF_PLAYERS entries
	 */
	public PlayerManager(Map<Player, Coordinate> playerList)
			throws IllegalNumberOfPlayersException {
		if (playerList.size() != NUMBER_OF_PLAYERS)
			throw new IllegalNumberOfPlayersException();

		this.playerList = playerList;
		this.currentPlayer = (Player) playerList.keySet().toArray()[0];
		this.currentNumberOfActions = 0;
	}
	
	/**
	 * Return the Coordinate of the current player.
	 * 
	 * @return
	 * 			A Coordinate object that is the coordinate of the current player.
	 */
	public Coordinate getCurrentPlayerCoordinate() {
		return this.playerList.get(currentPlayer);
	}
}
