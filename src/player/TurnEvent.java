package player;

/**
 * The TurnEvent is a signal that is sent to every observer which is interested
 * in the execution of an action.
 */
public enum TurnEvent {
	/**
	 * This indicates the end of a player's action, while staying the active
	 * player.
	 */
	END_ACTION,
	/**
	 * This indicates the end of a player's turn, i.e. a player switch is taking place.
	 */
	END_TURN,
	/**
	 * This indicates the end of the game, i.e. one of the players has won.
	 */
	END_GAME
}
