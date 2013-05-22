package game;

/**
 * An event sent by the {@link Game} when notifying its observers.
 */
public enum GameEvent {
	
	/**
	 * This indicates a player has won the game.
	 */
	PLAYER_WON,
	/**
	 * This indicates a player has lost the game.
	 */
	PLAYER_LOSE;
	
}
